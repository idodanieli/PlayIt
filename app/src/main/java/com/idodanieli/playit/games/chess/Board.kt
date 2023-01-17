package com.idodanieli.playit.games.chess

import kotlin.math.abs

class Board {
    private var piecesBox = mutableSetOf<Piece>()

    init {
        reset()
    }

    fun clear() {
        piecesBox.clear()
    }

    fun addPiece(piece: Piece) {
        piecesBox.add(piece)
    }

    private fun canKnightMove(from: Square, to: Square): Boolean {
        return abs(from.col - to.col) == 2 && abs(from.row - to.row) == 1 ||
                abs(from.col - to.col) == 1 && abs(from.row - to.row) == 2
    }

    private fun canRookMove(from: Square, to: Square): Boolean {
        if (from.col == to.col && isClearVerticallyBetween(from, to) ||
            from.row == to.row && isClearHorizontallyBetween(from, to)) {
            return true
        }
        return false
    }

    private fun isClearVerticallyBetween(from: Square, to: Square): Boolean {
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

    private fun isClearHorizontallyBetween(from: Square, to: Square): Boolean {
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

    private fun isClearDiagonally(from: Square, to: Square): Boolean {
        if (abs(from.col - to.col) != abs(from.row - to.row)) return false
        val gap = abs(from.col - to.col) - 1
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(nextCol, nextRow) != null) {
                return false
            }
        }
        return true
    }

    private fun canBishopMove(from: Square, to: Square): Boolean {
        if (abs(from.col - to.col) == abs(from.row - to.row)) {
            return isClearDiagonally(from, to)
        }
        return false
    }

    private fun canQueenMove(from: Square, to: Square): Boolean {
        return canRookMove(from, to) || canBishopMove(from, to)
    }

    private fun canKingMove(from: Square, to: Square): Boolean {
        if (canQueenMove(from, to)) {
            val deltaCol = abs(from.col - to.col)
            val deltaRow = abs(from.row - to.row)
            return deltaCol == 1 && deltaRow == 1 || deltaCol + deltaRow == 1
        }
        return false
    }

    private fun canPawnMove(from: Square, to: Square): Boolean {
        if (from.col == to.col) {
            if (from.row == 1) {
                return to.row == 2 || to.row == 3
            } else if (from.row == 6) {
                return to.row == 5 || to.row == 4
            }
        }
        return false
    }


    fun canMove(from: Square, to: Square): Boolean {
        if (from.col == to.col && from.row == to.row) {
            return  false
        }
        val movingPiece = pieceAt(from) ?: return false
        return when(movingPiece.type) {
            Type.KNIGHT -> canKnightMove(from, to)
            Type.ROOK -> canRookMove(from, to)
            Type.BISHOP -> canBishopMove(from, to)
            Type.QUEEN -> canQueenMove(from, to)
            Type.KING -> canKingMove(from, to)
            Type.PAWN -> canPawnMove(from, to)
        }
    }

    fun movePiece(from: Square, to: Square) {
        if (canMove(from, to)) {
            movePiece(from.col, from.row, to.col, to.row)
        }
    }

    private fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        if (fromCol == toCol && fromRow == toRow) return
        val movingPiece = pieceAt(fromCol, fromRow) ?: return

        pieceAt(toCol, toRow)?.let {
            if (it.player == movingPiece.player) {
                return
            }
            piecesBox.remove(it)
        }

        piecesBox.remove(movingPiece)
        addPiece(movingPiece.copy(col = toCol, row = toRow))
    }

    fun reset() {
        clear()
        for (i in 0 until 2) {
            addPiece(Piece(0 + i * 7, 0, Player.WHITE, Type.ROOK))
            addPiece(Piece(0 + i * 7, 7, Player.BLACK, Type.ROOK))

            addPiece(Piece(1 + i * 5, 0, Player.WHITE, Type.KNIGHT))
            addPiece(Piece(1 + i * 5, 7, Player.BLACK, Type.KNIGHT))

            addPiece(Piece(2 + i * 3, 0, Player.WHITE, Type.BISHOP))
            addPiece(Piece(2 + i * 3, 7, Player.BLACK, Type.BISHOP))
        }

        for (i in 0 until 8) {
            addPiece(Piece(i, 1, Player.WHITE, Type.PAWN))
            addPiece(Piece(i, 6, Player.BLACK, Type.PAWN))
        }

        addPiece(Piece(3, 0, Player.WHITE, Type.QUEEN))
        addPiece(Piece(3, 7, Player.BLACK, Type.QUEEN))
        addPiece(Piece(4, 0, Player.WHITE, Type.KING))
        addPiece(Piece(4, 7, Player.BLACK, Type.KING))
    }

    fun pieceAt(square: Square): Piece? {
        return pieceAt(square.col, square.row)
    }

    private fun pieceAt(col: Int, row: Int): Piece? {
        for (piece in piecesBox) {
            if (col == piece.col && row == piece.row) {
                return  piece
            }
        }
        return null
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
            desc += pieceAt(col, row)?.let { piece ->
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