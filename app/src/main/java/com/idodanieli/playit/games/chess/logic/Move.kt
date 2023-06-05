package com.idodanieli.playit.games.chess.logic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Move(
    @SerialName("origin") val origin: Square,
    @SerialName("dest")   val dest: Square,
    @SerialName("player") val player: Player,

    // followUpMoves to apply after this move ( Example use: Castling )
    @SerialName("followUpMoves") val followUpMoves: List<Move> = emptyList()
) {
    companion object {
        fun fromJSON(json: String): Move {
            return Json.decodeFromString(json)
        }
    }

    override fun toString(): String {
        return "$player $origin -> $dest"
    }

    fun toJson(): String {
        return  Json.encodeToString(this)
    }

    fun flipVertically(boardSize: Int): Move {
        return Move(origin, dest.flipVertically(boardSize), player)
    }
}