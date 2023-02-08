package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Player
import com.idodanieli.playit.games.chess.Square

abstract class Rider(square: Square, player: Player) : BasePiece(square, player) {
    override val movementType = MovementType.RIDER

    abstract fun possibleMoves(board: Board, getMovesInDirection: (board: Board, direction: Square, max_steps: Int) -> List<Square>): List<Square>

    override fun xrayPossibleMove(board: Board): List<Square> {
        return possibleMoves(board, ::getXrayMovesInDirection)
    }

    override fun possibleMoves(board: Board): List<Square> {
        return possibleMoves(board, ::getAllAvailableMovesInDirection)
    }
}