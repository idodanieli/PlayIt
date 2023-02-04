package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*
import kotlin.math.abs

private val moveOffsets = arrayOf(-1, 0, 1)

class King(square: Square, player: Player, type: Type) : BasePiece(square, player, type) {

    override fun canMove(destination: Square, board: Board): Boolean {
        if (canQueenMove(this.square, destination, board)) {
            val deltaCol = abs(this.square.col - destination.col)
            val deltaRow = abs(this.square.row - destination.row)
            return deltaCol == 1 && deltaRow == 1 || deltaCol + deltaRow == 1
        }
        return false
    }

    override fun availableSquares(board: Board): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                if (i == 0 && j == 0) { continue }

                val move = Square(square.col + i, square.col + j)
                if (board.isIn(move)) {
                    moves.add(move)
                }
            }
        }

        return moves
    }

}