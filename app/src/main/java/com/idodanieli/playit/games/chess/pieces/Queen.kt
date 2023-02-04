package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private val moveOffsets = arrayOf(1, -1, 0)

class Queen(square: Square, player: Player, type: Type) : BasePiece(square, player, type) {

    override fun availableSquares(board: Board): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                if (i == 0 && j == 0) { continue }

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