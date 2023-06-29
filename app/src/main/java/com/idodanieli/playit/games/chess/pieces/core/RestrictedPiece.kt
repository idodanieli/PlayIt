package com.idodanieli.playit.games.chess.pieces.core

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece

// RestrictedPiece is a piece that can move to certain squares but not attack them
open class RestrictedPiece(square: Square, player: Player): BasePiece(square, player) {

    override fun availableMoves(board: Board): List<Move> {
        val moveSquares = this.availableSquares(board)
        val captureSquares = this.capturableSquares(board)
        val restrictedSquares = moveSquares.subtract(captureSquares.toSet())

        return super.availableMoves(board).filter {
            isValidMove(it, board, restrictedSquares)
        }
    }

    private fun isValidMove(move: Move, board: Board, restrictedSquares: Set<Square>): Boolean {
        if (move.dest in restrictedSquares) {
            return notACaptureMove(move, board)
        }

        return true
    }

    private fun notACaptureMove(move: Move, board: Board): Boolean {
        return board.pieceAt(move.dest) == null
    }
}