package com.idodanieli.playit.games.chess

import com.idodanieli.playit.games.chess.pieces.*
import kotlin.math.abs

class Board(private var piecesBox: MutableSet<Piece>, private var size: Int) {

    fun isClearVerticallyBetween(from: Square, to: Square): Boolean {
        if (from.col != to.col) return false
        val gap = abs(from.row - to.row) - 1
        if (gap == 0 ) return true
        for (i in 1..gap) {
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(Square(from.col, nextRow)) != null) {
                return false
            }
        }
        return true
    }

    fun isClearHorizontallyBetween(from: Square, to: Square): Boolean {
        if (from.row != to.row) return false
        val gap = abs(from.col - to.col) - 1
        if (gap == 0 ) return true
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            if (pieceAt(Square(nextCol, from.row)) != null) {
                return false
            }
        }
        return true
    }

    fun isClearDiagonally(from: Square, to: Square): Boolean {
        if (abs(from.col - to.col) != abs(from.row - to.row)) return false
        val gap = abs(from.col - to.col) - 1
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            val nextSquare = Square(nextCol, nextRow)
            if (pieceAt(nextSquare) != null) {
                return false
            }
        }
        return true
    }

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

    override fun toString(): String {
        var desc = " \n"
        for (row in 7 downTo 0) {
            desc += "$row"
            desc += boardRow(row)
            desc += "\n"
        }
        desc += "  0 1 2 3 4 5 6 7"

        return desc
    }

    private fun boardRow(row: Int) : String {
        var desc = ""
        for (col in 0 until 8) {
            desc += " "
            desc += pieceAt(Square(col, row))?.let { piece ->
                val white = piece.player == Player.WHITE
                when (piece.type) {
                    Type.KING -> if (white) "k" else "K"
                    Type.QUEEN -> if (white) "q" else "Q"
                    Type.BISHOP -> if (white) "b" else "B"
                    Type.ROOK -> if (white) "r" else "R"
                    Type.KNIGHT -> if (white) "n" else "N"
                    Type.PAWN -> if (white) "p" else "P"
                }
            } ?: "."
        }
        return desc
    }
}