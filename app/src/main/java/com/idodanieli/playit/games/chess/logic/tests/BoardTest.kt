package com.idodanieli.playit.games.chess.logic.tests

import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.classic.Pawn
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
}