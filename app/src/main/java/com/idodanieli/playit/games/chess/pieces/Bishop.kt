package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*
import kotlin.math.abs

private val moveOffsets = arrayOf(1, -1)

class Bishop(square: Square, player: Player, type: Type) : BasePiece(square, player, type) {

    override fun canMove(destination: Square, board: Board): Boolean {
       return canBishopMove(this.square, destination, board)
    }

    override fun availableSquares(board: Board): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                var col = square.col + i
                var row = square.row + j

                while (board.isIn(Square(col, row))) {
                    moves.add(Square(col, row))
                    col += i
                    row += j
                }
            }
        }

        return moves
    }
}

fun canBishopMove(source: Square, destination: Square, board: Board): Boolean {
    if (abs(source.col - destination.col) == abs(source.row - destination.row)) {
        return board.isClearDiagonally(source, destination)
    }
    return false
}