package com.idodanieli.playit.games.chess.pieces.core

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Player
import com.idodanieli.playit.games.chess.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.MovementType
import com.idodanieli.playit.games.chess.pieces.Piece

abstract class Rider(square: Square, player: Player) : BasePiece(square, player) {
    override val movementType = MovementType.RIDER

    abstract fun possibleMoves(board: Board, getMovesInDirection: (self: Piece, board: Board, direction: Square, max_steps: Int) -> List<Square>): List<Square>

    override fun xrayPossibleMove(board: Board): List<Square> {
        return possibleMoves(board, ::xrayMovesInDirection)
    }

    override fun possibleMoves(board: Board): List<Square> {
        return possibleMoves(board, ::allMovesInDirecton)
    }
}