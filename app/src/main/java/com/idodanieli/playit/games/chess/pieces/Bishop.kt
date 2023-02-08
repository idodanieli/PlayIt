package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private val moveOffsets = arrayOf(1, -1)

class Bishop(square: Square, player: Player) : Rider(square, player) {
    override val type = Type.BISHOP
    override val movementType = MovementType.RIDER

    override fun possibleMoves(board: Board, getMovesInDirection: (board: Board, direction: Square, max_steps: Int) -> List<Square>): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                val direction = Square(i, j)
                moves.addAll(getMovesInDirection(board, direction, NO_MAX_STEPS))
            }
        }

        return moves
    }
}
