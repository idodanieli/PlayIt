package com.idodanieli.playit.games.chess.ui

import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece

data class TouchData(
    val square: Square,
    val piece: Piece,
    val availableMoves: Map<Move, Move>,
    var touches: Int = 1
) {
    companion object {
        const val PREVIEW_ABILITY_TOUCH_AMOUNT = 2
    }

    override fun equals(other: Any?): Boolean {
        if (other !is TouchData) return false

        return square == other.square
    }

    fun isPreviewAbilityTouch(): Boolean {
        return touches == PREVIEW_ABILITY_TOUCH_AMOUNT
    }
}