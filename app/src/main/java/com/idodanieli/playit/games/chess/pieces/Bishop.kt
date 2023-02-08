package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private val moveOffsets = arrayOf(1, -1)

class Bishop(square: Square, player: Player) : BasePiece(square, player) {
    override val type = Type.BISHOP
    override val movementType = MovementType.RIDER

    override fun xrayPossibleMove(board: Board): List<Square> {
        return possibleMoves(board, ::getXrayMovesInDirection)
    }

    override fun possibleMoves(board: Board): List<Square> {
        return possibleMoves(board, ::getAllAvailableMovesInDirection)
    }

    fun possibleMoves(board: Board, getMovesInDirection: (board: Board, direction: Square) -> List<Square>): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                val direction = Square(i, j)
                moves.addAll(getMovesInDirection(board, direction))
            }
        }

        return moves
    }
}
