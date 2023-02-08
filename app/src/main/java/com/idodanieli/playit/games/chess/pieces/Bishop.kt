package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private val moveOffsets = arrayOf(1, -1)

class Bishop(square: Square, player: Player) : BasePiece(square, player) {
    override val type = Type.BISHOP

    override fun possibleMoves(board: Board): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                val direction = Square(i, j)
                moves.addAll(getAllAvailableMovesInDirection(board, direction))
            }
        }

        return moves
    }
}
