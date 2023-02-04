package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*
import kotlin.math.abs

private val moveOffsets = arrayOf(1, -1)

class Bishop(square: Square, player: Player, type: Type) : BasePiece(square, player, type) {

    override fun availableSquares(board: Board): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                var col = square.col + i
                var row = square.row + j

                while (board.isIn(Square(col, row))) {
                    val move = Square(col, row)

                    // stops this direction if a piece is blocking
                    if (!board.isFree(move)) { break }

                    moves.add(move)
                    col += i
                    row += j
                }
            }
        }

        return moves
    }
}
