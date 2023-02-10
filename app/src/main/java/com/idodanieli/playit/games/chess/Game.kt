package com.idodanieli.playit.games.chess

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
            for (piece in board.pieces.filter { it.player != currentPlayer}) {
                if (!piece.possibleCheckBlockingMoves(board).isEmpty()) { return false }
            }

            return true
        }

        return false
   }
}