package com.idodanieli.playit.games.chess.pieces.classic

import com.idodanieli.playit.games.chess.*
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.core.MovementType

private val KNIGHT_MOVE_OFFSETS = arrayOf(1, 2, -1, -2)
const val TYPE_KNIGHT = "N"

open class Knight(square: Square, player: Player) : BasePiece(square, player) {
    override val type = TYPE_KNIGHT
    override val movementType = MovementType.LEAPER
    open val moveOffsets = KNIGHT_MOVE_OFFSETS

    override fun possibleMoves(board: Board): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                if (Math.abs(i) == Math.abs(j)) { continue }

                // TODO: Fix This -> Its redundant
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