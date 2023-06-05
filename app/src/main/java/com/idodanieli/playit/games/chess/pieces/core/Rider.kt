package com.idodanieli.playit.games.chess.pieces.core

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece

abstract class Rider(square: Square, player: Player) : BasePiece(square, player) {
    override val movementType = MovementType.RIDER

    abstract fun possibleMoves(board: Board, getMovesInDirection: (self: Piece, board: Board, direction: Square, max_steps: Int) -> List<Move>): List<Move>

    override fun xrayPossibleMove(board: Board): List<Move> {
        return possibleMoves(board, ::xrayMovesInDirection)
    }

    override fun possibleMoves(board: Board): List<Move> {
        return possibleMoves(board, ::allMovesInDirection)
    }
}