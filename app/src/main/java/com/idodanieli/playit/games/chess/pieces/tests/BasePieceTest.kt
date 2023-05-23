package com.idodanieli.playit.games.chess.pieces.tests

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.Pawn
import com.idodanieli.playit.games.chess.pieces.classic.Queen
import org.junit.Test

class BasePieceTest {

    @Test // Tests that the canBeCaptured function works
    fun testCanBeCaptured() {
        val pawn = Pawn(Square(0, 6), Player.BLACK)

        // Tests that the pawn can be captured when threatened
        val enemy = Queen(Square(0, 0), Player.WHITE)
        val canBeCapturedBoard = Board(mutableSetOf(pawn, enemy), 8)
        assert(pawn.canBeCaptured(canBeCapturedBoard)) { errorFormat(canBeCapturedBoard, "Evaluated that $pawn can't be captured") }

        // Tests that the pawn CANT be captured after the threatening queen has been removed
        canBeCapturedBoard.remove(enemy)
        assert(!pawn.canBeCaptured(canBeCapturedBoard)) { errorFormat(canBeCapturedBoard, "Evaluated that $pawn can be captured, even after the queen was removed") }

        // Tests that the pawn can't be captured when not threatened
        val farAwayEnemy = Pawn(Square(1, 0), Player.WHITE)
        val cantBeCapturedBoard = Board(mutableSetOf(pawn, farAwayEnemy), 8)
        assert(!pawn.canBeCaptured(cantBeCapturedBoard)) { errorFormat(cantBeCapturedBoard, "Evaluated that $pawn can be captured") }
    }
}