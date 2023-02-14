package com.idodanieli.playit.games.chess

import android.util.Log
import com.idodanieli.playit.games.chess.pieces.*

data class Game(var name: String, private var pieces: MutableSet<Piece>, var size: Int) {
    val board = Board(pieces, size)
    var currentPlayer = Player.WHITE // white always starts in chess

    fun pieces(): Set<Piece> {
        return this.pieces
    }

    fun canMove(from: Square, to: Square): Boolean {
        if (from == to) {
            return  false
        }
        val movingPiece = board.pieceAt(from) ?: return false
        if (movingPiece.player != currentPlayer) { return false }

        return to in movingPiece.validMoves(this.board, ignoreSamePlayer = false)
    }

    fun movePiece(from: Square, to: Square) {
        if (from == to) return
        val movingPiece = this.board.pieceAt(from) ?: return

        board.movePiece(movingPiece, to)
    }

    fun isOver(): Boolean {
        if (board.isChecked(currentPlayer.opposite())) {
            Log.d("isOver()", "${currentPlayer.opposite()} is checked")

            for (piece in board.pieces.filter { it.player != currentPlayer}) {
                val blockingMoves = piece.possibleCheckBlockingMoves(board)
                if (blockingMoves.isNotEmpty()) {
                    Log.d("isOver()", "FALSE - $piece can block the check - $blockingMoves")
                    return false
                }
            }

            Log.d("isOver()", "TRUE - ${currentPlayer.opposite()} cant move!")
            return true
        }

        Log.d("isOver()", "FALSE - ${currentPlayer.opposite()} is not checked...")
        return false
   }
}