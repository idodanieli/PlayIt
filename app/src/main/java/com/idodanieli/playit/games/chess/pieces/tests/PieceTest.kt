package com.idodanieli.playit.games.chess.pieces.tests

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.Pawn
import com.idodanieli.playit.games.chess.pieces.classic.Queen
import org.junit.Test

class PieceTest {

    @Test // Tests that the canBeCaptured function works
    fun testCanBeCaptured() {
        val pawn = Pawn(Square(0, 6), Player.BLACK)
        val enemy = Queen(Square(0, 0), Player.WHITE)
        val farAwayEnemy = Pawn(Square(1, 0), Player.WHITE)

        val canBeCapturedBoard = Board(mutableSetOf(pawn, enemy), 8)
        val cantBeCapturedBoard = Board(mutableSetOf(pawn, farAwayEnemy), 8)

        assert(pawn.canBeCaptured(canBeCapturedBoard)) { errorFormat(canBeCapturedBoard, "Evaluated that $pawn can't be captured") }
        assert(!pawn.canBeCaptured(cantBeCapturedBoard)) { errorFormat(cantBeCapturedBoard, "Evaluated that $pawn can be captured") }
    }
}