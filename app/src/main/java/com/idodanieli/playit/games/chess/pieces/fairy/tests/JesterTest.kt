package com.idodanieli.playit.games.chess.pieces.fairy.tests

import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.logic.Game
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.pieces.fairy.Jester
import com.idodanieli.playit.games.chess.pieces.tests.errorFormat
import org.junit.Test

class JesterTest {

    @Test
    fun testMovement() {
        val jesterStartingSquare = Square(0, 0)
        val knightStartingSquare = Square(7, 7)

        val wJester = Jester(jesterStartingSquare, Player.WHITE)
        val bKnight = Knight(knightStartingSquare, Player.BLACK)

        val game = Game("", setOf(wJester, bKnight), CHESSBOARD_SIZE)

        val bKnightDestination = Square(5, 6)
        val move = Move(knightStartingSquare, bKnightDestination)

        game.applyMove(move)

        val wKnight = Knight(jesterStartingSquare, Player.WHITE)

        val wJesterDestinations = wJester.availableSquares(game.board)
        assert(wJesterDestinations == wKnight.availableSquares(game.board)) {
            errorFormat(game.board, "$wJester should have mimicked the black knights movement\n" +
                    "instead he could move to: $wJesterDestinations")
        }
    }
}