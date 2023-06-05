package com.idodanieli.playit.games.chess.pieces.core

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece

abstract class Rider(square: Square, player: Player) : BasePiece(square, player) {
    override val movementType = MovementType.RIDER

    abstract fun possibleMoves(board: Board, getMovesInDirection: (self: Piece, board: Board, direction: Square, max_steps: Int) -> List<Square>): List<Square>

    override fun xrayAvailableMoves(board: Board): List<Square> {
        return possibleMoves(board, ::xrayMovesInDirection)
    }

    override fun availableSquares(board: Board): List<Square> {
        return possibleMoves(board, ::allMovesInDirecton)
    }
}