package com.idodanieli.playit.games.chess.logic

import kotlinx.serialization.Serializable

@Serializable
data class Move(
    val origin: Square,
    val dest: Square,

    // followUpMoves to apply after this move ( Example use: Castling )
    val followUpMoves: List<Move> = emptyList(),
    val isAbilityMove: Boolean = false
) {

    override fun toString(): String {
        return "$origin -> $dest"
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