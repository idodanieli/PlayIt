package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.pieces.*
import com.idodanieli.playit.games.chess.pieces.classic.TYPE_KING

class Board(var pieces: MutableSet<Piece>, var size: Int) {
    var map = pieces.associateBy { it.square }.toMutableMap()
    var whitePieces = pieces.filter { it.player == Player.WHITE }
    var blackPieces = pieces.filter { it.player == Player.BLACK }

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
        // TODO: This can be way more efficient with a map instead of a list
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
    fun piece(type: String, player: Player): Piece? {
        for (piece in pieces) {
            if (piece.player == player && piece.type == type) {
                return piece
            }
        }

        return null
    }

    fun pieces(type: String, player: Player): List<Piece> {
        return  pieces.filter { it.type == type && it.player == player }
    }

    // isChecked returns true if the given player is checked
    fun isChecked(player: Player): Boolean {
        val king = piece(TYPE_KING, player)
        king?.let { return it.canBeCaptured(this) }

        return false
    }

    // isIn returns true if the given square is in the boards borders
    fun isIn(square: Square): Boolean {
        return square.col in 0 until this.size && square.row in 0 until this.size
    }

    // isFree returns true if the given square doesn't contain a piece
    fun isFree(square: Square): Boolean {
        return pieceAt(square) == null
    }

    fun isFree(squares: List<Square>): Boolean {
        for(square in squares) {
            if (!isFree(square)) { return false }
        }

        return true
    }

    // isThreatened returns true if the square is threatened by another piece
    fun isThreatened(square: Square, enemy: Player): Boolean {
        val enemyPieces = this.pieces.filter { it.player == enemy }
        for (piece in enemyPieces) {
            if (piece.type == TYPE_KING) { // To avoid recursion
                if (piece.square.isNear(square)) return true
                continue
            }

            if (square in piece.captureMoves(this, ignoreSamePlayer = true)) {
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
        if (piece.type == TYPE_KING) { return null } // TODO: Make this more general

        val king = piece(TYPE_KING, piece.player) ?: return null
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

    private fun flatString(pieces: List<Piece>) : String {
        val flatBoardCharcters = ".".repeat(size * size).toCharArray()
        for (piece in pieces) {
            val type = if (piece.player == Player.WHITE) piece.type else piece.type.lowercase()
            flatBoardCharcters[piece.square.row * size + piece.square.col] = type[0]
        }

        return String(flatBoardCharcters)
    }

    private fun flatToPrettyPrint(flat: String) : String {
        var prettyCharacters = charArrayOf()

        for (idx in flat.indices) {
            if (idx % size == 0) {
                prettyCharacters += '\n'
            }

            prettyCharacters += flat[idx]
            prettyCharacters += ' '
        }

        return String(prettyCharacters)
    }

    override fun toString() : String {
        return flatToPrettyPrint(flatString(pieces.toList()))
    }

    fun getBitboard(type: String, player: Player) : String {
        return flatToPrettyPrint(flatString(pieces(type, player)))
    }
}