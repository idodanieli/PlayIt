package com.idodanieli.playit.games.chess.pieces.fairy.tests

import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.Bishop
import com.idodanieli.playit.games.chess.pieces.classic.King
import com.idodanieli.playit.games.chess.pieces.classic.Rook
import com.idodanieli.playit.games.chess.pieces.fairy.Cannon
import com.idodanieli.playit.games.chess.pieces.tests.errorFormat
import com.idodanieli.playit.games.chess.pieces.tests.listsContainSameValues
import com.idodanieli.playit.games.chess.variants.ClassicGame
import org.junit.Test

class CannonTest {
    // C stands for white cannon
    // can capture the bishop or the rook at the first row
    // can move to the spots marked with x
    // . . . x . . . .
    // . . . x . . . .
    // . . . x . . . .
    // r b K C x x x x
    // . . . x . . . .
    // . . . k . . . .
    // . . . . . . . .
    // . . . r . . . .
    private val wCannon = Cannon(Square(3, 4), Player.WHITE)
    private val wKing = King(Square(2, 4), Player.WHITE)
    private val bBishop = Bishop(Square(1, 4), Player.BLACK)
    private val bRook1 = Rook(Square(0, 4), Player.BLACK)
    private val bRook2 = Rook(Square(3, 0), Player.BLACK)
    private val bKing = King(Square(3, 2), Player.BLACK)
    
    private val pieces = setOf(wCannon, wKing, bBishop, bRook1, bRook2, bKing)
    private val game = ClassicGame("", pieces)
    
    @Test
    fun testAvailableSquares() {
        val expectedResult = listOf(
            Square(3, 3),
            Square(3, 5),
            Square(3, 6),
            Square(3, 7),
            Square(4, 4),
            Square(5, 4),
            Square(6, 4),
            Square(7, 4),
        )

        val result = wCannon.availableSquares(game.board)

        assert( listsContainSameValues( result, expectedResult)) {
            errorFormat(game.board, "Cannon should be able to move to: $expectedResult\n" +
                    "instead of ")
        }
    }
    
    @Test
    fun testCapturableSquares() {
        val expectedResult = listOf(
            bBishop.square,
            bRook2.square
        )

        val result = wCannon.capturableSquares(game.board)

        assert( listsContainSameValues(result, expectedResult)) {
            errorFormat(game.board, "Cannon should be able to capture at: $expectedResult\n" +
                    "instead of: $result")
        }
    }
}
