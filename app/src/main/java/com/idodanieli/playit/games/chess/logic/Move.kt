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

    // followUpMoves to apply after this move ( Example use: Castling )
    @SerialName("followUpMoves") val followUpMoves: List<Move> = emptyList()
) {
    companion object {
        fun fromJSON(json: String): Move {
            return Json.decodeFromString(json)
        }
    }

    override fun toString(): String {
        return "$origin -> $dest"
    }

    fun toJson(): String {
        return  Json.encodeToString(this)
    }

    // --- Map Key Calculation ---------------------------------------------------------------------
    override fun hashCode(): Int {
        return Pair(origin, dest).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) { return true }
        if (other !is Move) { return false }

        return this.origin == other.origin &&
                this.dest == other.dest
    }
}