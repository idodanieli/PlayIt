package com.idodanieli.playit.games.chess

import org.json.JSONObject

private const val NAME = "name"
private const val BOARD = "board"

class GameParser {
    fun parse(json: JSONObject): com.idodanieli.playit.Game {
        val name = json.getString(NAME)
        val board = json.getString(BOARD)

        return com.idodanieli.playit.Game(name)
    }
}