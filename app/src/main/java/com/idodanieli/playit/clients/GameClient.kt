package com.idodanieli.playit.clients

import android.util.Log
import com.idodanieli.playit.SharedPrefsManager
import com.idodanieli.playit.games.chess.logic.Move

class GameClient private constructor(address: String) {
    private val client = HTTPClient(address)
    private lateinit var gameID: String

    companion object {
        private const val URI_CREATE_GAME = "/create"
        private const val URI_JOIN_GAME = "/join"
        private const val URI_GAME_MOVE = "/game/move"
        private const val URI_GAME_LAST_MOVE = "/game/last_move"

        private const val PARAM_GAME_ID = "game_id"
        private const val PARAM_CREATOR = "creator"
        private const val PARAM_USERNAME = "username"

        private const val STATUS_NO_MOVES = "NO_MOVES"

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
        val username = SharedPrefsManager.getInstance().getUsername()
        val gameID = client.get(URI_CREATE_GAME, params = mapOf(
            PARAM_CREATOR to username
        ))

        this.gameID = gameID // Sets the game_id for the client

        return gameID
    }

    fun join(game_id: String): String {
        val username = SharedPrefsManager.getInstance().getUsername()

        this.gameID = game_id

        return client.get(
            uri = URI_JOIN_GAME,
            params = mapOf(
                PARAM_GAME_ID to game_id,
                PARAM_USERNAME to username
            )
        )
    }

    fun movePiece(move: Move) {
        val response = client.post(
            uri = URI_GAME_MOVE,
            body = move.toJson(),
            params = mapOf(PARAM_GAME_ID to gameID)
        )

        Log.d("GameClient", response)
    }

    fun getLastMove(): Move? {
        val lastMove = client.get(
            uri = URI_GAME_LAST_MOVE,
            params = mapOf(PARAM_GAME_ID to gameID)
        )

        if (lastMove == STATUS_NO_MOVES) {
            return null
        }

        return Move.fromJSON(lastMove)
    }
}