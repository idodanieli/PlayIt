package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private val moveOffsets = arrayOf(1, -1, 0)

class Queen(square: Square, player: Player) : BasePiece(square, player) {
    override val type = Type.QUEEN
    override val movementType = MovementType.RIDER

    override fun xrayPossibleMove(board: Board): List<Square> {
        return possibleMoves(board, ::getXrayMovesInDirection)
    }

    override fun possibleMoves(board: Board): List<Square> {
       return possibleMoves(board, ::getAllAvailableMovesInDirection)
    }

    private fun possibleMoves(board: Board, getMovesInDirection: (board: Board, direction: Square) -> List<Square>): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                if (i == 0 && j == 0) { continue }

                val direction = Square(i, j)
                moves.addAll(getMovesInDirection(board, direction))
            }
        }

        return moves
    }
}