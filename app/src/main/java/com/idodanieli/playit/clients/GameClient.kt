package com.idodanieli.playit.clients

import android.util.Log
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

        private const val STATUS_SUCCESS = "SUCCESS"

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
        var gameID = ""
        val thread = Thread {
            gameID = client.get(URI_CREATE_GAME)
        }

        thread.start()
        thread.join()

        this.gameID = gameID // Sets the game_id for the client

        return gameID
    }

    fun join(game_id: String): String {
        var response = ""

        val thread = Thread {
            response = client.get(
                uri = URI_JOIN_GAME,
                params = mapOf(PARAM_GAME_ID to game_id)
            )
        }

        thread.start()
        thread.join()

        return response
    }

    fun movePiece(move: Move) {
        Thread {
            val response = client.post(
                uri = URI_GAME_MOVE,
                body = move.toJson(),
                params = mapOf(PARAM_GAME_ID to gameID)
            )

            Log.d("GameClient", response)
        }.start()
    }

    fun getLastMove(): Move {
        var lastMove = ""

        val thread = Thread {
            lastMove = client.get(
                uri = URI_GAME_LAST_MOVE,
                params = mapOf(PARAM_GAME_ID to gameID)
            )
        }

        thread.start()
        thread.join() // Wait for thread to end

        return Move.fromJSON(lastMove)
    }
}