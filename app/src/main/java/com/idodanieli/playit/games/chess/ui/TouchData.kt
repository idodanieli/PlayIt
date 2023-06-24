package com.idodanieli.playit.games.chess.ui

import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece

data class TouchData(
    val square: Square,
    val piece: Piece,
    val availableMoves: Map<Move, Move>,
    var isPieceFocused: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (other !is TouchData) return false

        return square == other.square
    }
}