package com.idodanieli.playit.clients

import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@kotlinx.serialization.Serializable
data class MoveInfo(val move: Move, val player: Player) {
    companion object {

        fun fromJSON(json: String): MoveInfo {
            return Json.decodeFromString(json)
        }

    }

    fun toJson(): String {
        return  Json.encodeToString(this)
    }

    fun isOpponentsMove(hero: Player): Boolean {
        return this.player != hero
    }
}