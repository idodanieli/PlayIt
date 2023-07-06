package com.idodanieli.playit.clients

import android.util.Log
import com.idodanieli.playit.User
import com.idodanieli.playit.games.chess.logic.Move

class GameClient private constructor(address: String) {
    private val client = HTTPClient(address)
    lateinit var gameID: String

    companion object {
        private const val URI_CREATE_GAME = "/create"
        private const val URI_JOIN_GAME = "/join"
        private const val URI_GAME_MOVE = "/game/move"
        private const val URI_GAME_LAST_MOVE = "/game/last_move"
        private const val URI_FIND_GAME = "/find_game"
        private const val URI_GET_OPPONENT = "/opponent"

        private const val PARAM_GAME_ID = "game_id"
        private const val PARAM_GAME_NAME = "game_name"
        private const val PARAM_CREATOR = "creator"
        private const val PARAM_USERNAME = "username"

        private const val STATUS_NO_MOVES = "NO_MOVES"

        const val DEFAULT_SLEEP_INTERVAL = 100L

        const val PLAYER_WHITE = "WHITE"

        @Volatile
        private var instance: GameClient? = null

        fun initialize(address: String) {
            if (instance == null) {
                instance = GameClient(address)
            }
        }

        fun getInstance(): GameClient {
            return instance!!
        }
    }

    // create a new game and return it's game_id
    fun create(): String {
        val username = User.getInstance().getUsername()
        val gameID = client.get(URI_CREATE_GAME, params = mapOf(
            PARAM_CREATOR to username
        ))

        this.gameID = gameID // Sets the game_id for the client

        return gameID
    }

    fun join(game_id: String): String {
        val username = User.getInstance().getUsername()

        this.gameID = game_id

        return client.get(
            uri = URI_JOIN_GAME,
            params = mapOf(
                PARAM_GAME_ID to game_id,
                PARAM_USERNAME to username
            )
        )
    }

    fun movePiece(moveInfo: MoveInfo) {
        val response = client.post(
            uri = URI_GAME_MOVE,
            body = moveInfo.toJson(),
            params = mapOf(PARAM_GAME_ID to gameID)
        )

        Log.d("GameClient", response)
    }

    fun getLastMove(): MoveInfo? {
        val lastMove = client.get(
            uri = URI_GAME_LAST_MOVE,
            params = mapOf(
                PARAM_GAME_ID to gameID,
                PARAM_USERNAME to User.getInstance().getUsername()
            )
        )

        if (lastMove == STATUS_NO_MOVES) {
            return null
        }

        return MoveInfo.fromJSON(lastMove)
    }

    fun getOpponent(interval: Long = DEFAULT_SLEEP_INTERVAL): String {
        var opponent = ""

        while (opponent.isEmpty()) {
            opponent = getOpponent()
            Thread.sleep(interval)
        }

        return opponent
    }

    private fun getOpponent(): String {
        return client.get(
            uri = URI_GET_OPPONENT,
            params = mapOf(
                PARAM_GAME_ID to gameID,
                PARAM_USERNAME to User.getInstance().getUsername()
            )
        )
    }

    fun findGame(gameName: String): String {
        return client.get( // Game ID
            uri = URI_FIND_GAME,
            params = mapOf(
                PARAM_USERNAME to User.getInstance().getUsername(),
                PARAM_GAME_NAME to gameName
            )
        )
    }
}