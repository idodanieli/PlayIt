package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private val moveOffsets = arrayOf(1, -1)

class Bishop(square: Square, player: Player, type: Type) : BasePiece(square, player, type) {

    override fun availableSquares(board: Board): List<Square> {
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
