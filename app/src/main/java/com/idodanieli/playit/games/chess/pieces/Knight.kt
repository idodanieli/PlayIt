package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private val KNIGHT_MOVE_OFFSETS = arrayOf(1, 2, -1, -2)

open class Knight(square: Square, player: Player) : BasePiece(square, player) {
    override val type = Type.KNIGHT
    override val movementType = MovementType.LEAPER
    open val moveOffsets = KNIGHT_MOVE_OFFSETS

    override fun possibleMoves(board: Board): List<Square> {
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