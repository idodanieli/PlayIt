package com.idodanieli.playit.clients

import android.util.Log

class GameClient(address: String) {
    private val client = HTTPClient(address)

    companion object {
        private const val URI_CREATE_GAME = "/create"
        private const val URI_JOIN_GAME = "/join"

        private const val PARAM_GAME_ID = "game_id"

        private const val STATUS_SUCCESS = "SUCCESS"
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
}