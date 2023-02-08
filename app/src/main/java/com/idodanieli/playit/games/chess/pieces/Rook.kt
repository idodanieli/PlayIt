package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private val moveOffsets = arrayOf(1, -1, 0)

class Rook(square: Square, player: Player) : Rider(square, player) {
    override val type = Type.ROOK

    override fun possibleMoves(board: Board, getMovesInDirection: (board: Board, direction: Square,  max_steps: Int) -> List<Square>): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                if (i == 0 && j == 0 || Math.abs(i) == Math.abs(j)) { continue }

                val direction = Square(i, j)
                moves.addAll(getMovesInDirection(board, direction, NO_MAX_STEPS))
            }
        }

        return moves
    }
}