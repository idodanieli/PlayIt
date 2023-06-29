package com.idodanieli.playit.games.chess.pieces.fairy.tests

import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.Queen
import com.idodanieli.playit.games.chess.pieces.fairy.Spartan
import com.idodanieli.playit.games.chess.pieces.tests.errorFormat
import com.idodanieli.playit.games.chess.variants.ClassicGame
import org.junit.Test

class SpartanTest {

    private val whiteSpartanOriginSquare = Square(0, 1)
    private val blackSpartanOriginSquare = Square(0, 6)

    private val whiteSpartan = Spartan(whiteSpartanOriginSquare, Player.WHITE)
    private val blackSpartan = Spartan(blackSpartanOriginSquare, Player.BLACK)

    @Test
    fun testMovement() {
        val game = ClassicGame("", setOf(whiteSpartan), CHESSBOARD_SIZE)

        val destinations = game.getLegalMovesForPiece(whiteSpartan).map { it.dest }
        val expectedDestinations = listOf(Square(0,0), Square(0, 2))

        assert(destinations.size == expectedDestinations.size) {
            errorFormat(game.board, "$whiteSpartan could move to $destinations instead of $expectedDestinations")
        }

        for (dest in expectedDestinations) {
            assert(dest in destinations) {
                errorFormat(game.board, "$whiteSpartan should be able to move to $dest")
            }
        }
    }

    @Test
    fun testCanNotCaptureBehind() {
        // Behind black spartan
        val whiteQueen = Queen(Square(0, 7), Player.WHITE)

        val game = ClassicGame("", setOf(blackSpartan, whiteQueen), CHESSBOARD_SIZE)

        val destinations = game.getLegalMovesForPiece(blackSpartan).map { move -> move.dest }

        assert(whiteQueen.square !in destinations) {
            errorFormat(game.board, "spartan can't capture a piece behind it!")
        }
    }

    @Test
    fun testCanCaptureInFront() {
        // In front of black spartan
        val whiteQueen = Queen(Square(0, 5), Player.WHITE)

        val game = ClassicGame("", setOf(blackSpartan, whiteQueen), CHESSBOARD_SIZE)

        val destinations = game.getLegalMovesForPiece(blackSpartan).map { move -> move.dest }

        assert(whiteQueen.square in destinations) {
            errorFormat(game.board, "spartan can capture a piece in front of it!")
        }
    }
}