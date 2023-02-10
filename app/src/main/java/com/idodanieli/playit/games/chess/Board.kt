package com.idodanieli.playit.games.chess

import com.idodanieli.playit.games.chess.pieces.*

class Board(var pieces: MutableSet<Piece>, var size: Int) {

    fun movePiece(piece: Piece, dst: Square) {
        this.pieceAt(dst)?.let { enemyPiece ->
            if (enemyPiece.player == piece.player) {
                return
            }
            pieces.remove(enemyPiece)
            piece.onEat(enemyPiece)
        }

        piece.square = dst
        piece.onMove()
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

    // isChecked returns true if the given player is checked
    fun isChecked(player: Player): Boolean {
        val king = piece(Type.KING, player)
        king?.let { return it.isChecked(this) }

        return false
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

            if (square in piece.eatMoves(this, ignoreSamePlayer = true)) {
                return true
            }
        }

        return false
    }

    private val MOVE_OFFSETS = arrayOf(-1, 0, 1)

    // getNeighborSquares returns all the squares near the given piece
    fun getAvailableNeighborSquares(piece: Piece): List<Square> {
        val squares = arrayListOf<Square>()

        for (i in MOVE_OFFSETS) {
            for (j in MOVE_OFFSETS) {
                if (i == 0 && j == 0) { continue }

                val square = Square(piece.square.col + i, piece.square.row + j)

                if (isIn(square) && playerAt(square) != piece.player) {
                    squares.add(square)
                }
            }
        }

        return squares
    }

    // getPinner returns the piece that pins the current piece
    fun getPinner(piece: Piece) : Piece? {
        if (piece.type == Type.KING) { return null } // TODO: Make this more general

        val king = piece(Type.KING, piece.player) ?: return null
        if (king.square == piece.square) { return null }
        val direction = king.square.directionTo(piece.square)

        var square = piece.square.copy()
        while (square.isValid(size)) {
            square += direction

            pieceAt(square)?.let {
                if(it.player == piece.player) { return null }
                if(king.square in it.xrayPossibleMove(this)) {
                    return it
                }
            }
        }

        return null
    }

    fun copy(): Board {
        return Board(pieces.toMutableSet(), size)
    }
}