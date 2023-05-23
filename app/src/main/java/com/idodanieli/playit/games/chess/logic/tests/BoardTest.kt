package com.idodanieli.playit.games.chess.logic.tests

import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.classic.*
import com.idodanieli.playit.games.chess.pieces.tests.errorFormat
import com.idodanieli.playit.games.chess.ui.CHESSBOARD_SIZE
import org.junit.Test

class BoardTest {

    @Test
    fun testPieceAt() {
        val square = Square(0, 1)
        val pawn = Pawn(square, Player.WHITE)
        val board = Board(mutableSetOf(pawn), CHESSBOARD_SIZE)
        var result = board.pieceAt(square)

        assert(result == pawn) { errorFormat(board,
            "pieceAt$square returned $result instead of $pawn") }

        val emptySquare = Square(0, 2)
        result = board.pieceAt(emptySquare)

        assert(result == null) { errorFormat(board,
            "pieceAt$square returned $result instead of null") }
    }

    @Test
    fun testGetPinner() {
        // k r b . . . . R # The black knight is pinned by the white queen
        // n p . . . . . . # and cannot move, the black pawn isn't pinned
        // . . . . . . . . # getPinner(bKnight) should return the white queen
        // . . . . . . . . # getPinner(bPawn)   should return null
        // . . . . . . . . # getPinner(bRook)   should return null ( because the bishop blocks )
        // Q . . . . . . . # getPinner(bBishop) should return null ( because the rook blocks )
        // . . . . . . . .
        // . . . . . . . .

        val bKing = King(Square(0, 0), Player.BLACK)
        val bKnight = Knight(Square(0, 1), Player.BLACK)
        val bPawn = Pawn(Square(1, 1), Player.BLACK)
        val bRook = Rook(Square(1, 0), Player.BLACK)
        val bBishop = Bishop(Square(2, 0), Player.BLACK)
        val wQueen = Queen(Square(0, 5), Player.WHITE)
        val wRook = Rook(Square(7, 0), Player.WHITE)

        val board = Board(mutableSetOf(bKing, bKnight, bPawn, bBishop, bRook, wQueen, wRook), CHESSBOARD_SIZE)

        val bKnightsPinner = board.getPinner(bKnight)
        assert(bKnightsPinner == wQueen) { errorFormat(board,
            "getPinner didn't return that $bKnight is pinned by $wQueen") }

        val bPawnsPinner = board.getPinner(bPawn)
        assert(bPawnsPinner == null) { errorFormat(board,
            "$bPawn pinned by $bPawnsPinner | instead of null") }

        val bRookPinner = board.getPinner(bRook)
        assert(bRookPinner == null) { errorFormat(board,
            "$bRook pinned by $bRookPinner | instead of null") }

        val bBishopPinner = board.getPinner(bBishop)
        assert(bBishopPinner == null) { errorFormat(board,
            "$bBishop pinned by $bBishopPinner | instead of null") }
    }
}