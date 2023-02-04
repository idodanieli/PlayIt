package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*
import kotlin.math.abs

private val moveOffsets = arrayOf(1, 2, -1, -2)

class Knight(square: Square, player: Player, type: Type) : BasePiece(square, player, type) {
    // TODO: Ugly Code...
    override fun canMove(destination: Square, board: Board): Boolean {
        return abs(this.square.col - destination.col) == 2 && abs(this.square.row - destination.row) == 1 ||
                abs(this.square.col - destination.col) == 1 && abs(this.square.row - destination.row) == 2
    }

    // availableMoves returns the
    override fun availableSquares(board: Board): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                if (Math.abs(i) == Math.abs(j)) { continue }

                val move1 = Square(this.square.col + i, this.square.row + j)
                val move2 = Square(this.square.col + j, this.square.row + i)

                for (move in listOf(move1, move2)) {
                    if (board.isIn(move)) { moves.add(move) }
                }
            }
        }

        return moves
    }
}