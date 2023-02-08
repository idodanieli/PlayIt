package com.idodanieli.playit.games.chess

import com.idodanieli.playit.games.chess.pieces.*

class Board(var pieces: MutableSet<Piece>, var size: Int) {

    fun canMove(from: Square, to: Square): Boolean {
        if (from == to) {
            return  false
        }
        val movingPiece = pieceAt(from) ?: return false

        return to in movingPiece.validMoves(this)
    }

    // pieceAt returns the piece at the given square. if there is none - returns null
    fun pieceAt(square: Square): Piece? {
        for (piece in pieces) {
            if (square == piece.square) {
                return  piece
            }
        }

        return null
    }

    // playerAt returns the player at the given square
    fun playerAt(square: Square): Player? {
        return pieceAt(square)?.player
    }

    // piece returns the first it finds that if of the given players and of the given type
    fun piece(type: Type, player: Player): Piece? {
        for (piece in pieces) {
            if (piece.player == player && piece.type == type) {
                return piece
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

    // isThreatened returns true if the square is threatened by another piece
    fun isThreatened(square: Square, enemy: Player): Boolean {
        val enemyPieces = this.pieces.filter { it.player == enemy }
        for (piece in enemyPieces) {
            if (piece.type == Type.KING) { // To avoid recursion
                if (piece.square.isNear(square)) return true
                continue
            }

            if (square in piece.validMoves(this)) {
                return true
            }
        }

        return false
    }

    fun copy(): Board {
        return Board(pieces.toMutableSet(), size)
    }
}