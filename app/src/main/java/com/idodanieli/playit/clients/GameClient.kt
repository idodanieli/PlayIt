package com.idodanieli.playit.clients

import android.util.Log
import com.idodanieli.playit.games.chess.logic.Square
import org.json.JSONObject

class GameClient private constructor(address: String) {
    private val client = HTTPClient(address)

    companion object {
        private const val URI_CREATE_GAME = "/create"
        private const val URI_JOIN_GAME = "/join"
        private const val URI__GAME_MOVE = "/game/move"

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
        return client.get(URI_CREATE_GAME)
    }

    fun join(game_id: String): Boolean {
        val response = client.get(URI_JOIN_GAME, params = mapOf(PARAM_GAME_ID to game_id))
        if (response == STATUS_SUCCESS) {
            return true
        }

        Log.d("GameClient", response)
        return false
    }

    fun movePiece(origin: Square, dest: Square) {
        val body = JSONObject()
        body.put("origin", origin)
        body.put("dest", dest)

        Thread {

            val response = client.post(URI__GAME_MOVE, body.toString()) // TODO: Add game_id as query param
            Log.d("GameClient", response)

        }.start()
    }
}