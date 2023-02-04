package com.idodanieli.playit.games.chess

import com.idodanieli.playit.games.chess.pieces.*

class Board(private var piecesBox: MutableSet<Piece>, private var size: Int) {

    fun canMove(from: Square, to: Square): Boolean {
        if (from == to) {
            return  false
        }
        val movingPiece = pieceAt(from) ?: return false

        return to in movingPiece.availableSquares(this)
    }

    // pieceAt returns the piece at the given square. if there is none - returns null
    fun pieceAt(square: Square): Piece? {
        for (piece in piecesBox) {
            if (square == piece.square) {
                return  piece
            }
        }

        return null
    }

    // isIn returns true if the given square is in the boards borders
    fun isIn(square: Square): Boolean {
        return square.col in 0..this.size && square.row in 0..this.size
    }

    // isFree returns true if the given square doesn't contain a piece
    fun isFree(square: Square): Boolean {
        return pieceAt(square) == null
    }

    // playerAt returns the player at the given square
    fun playerAt(square: Square): Player? {
        return pieceAt(square)?.player
    }
}