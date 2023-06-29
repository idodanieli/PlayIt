package com.idodanieli.playit.games.chess.pieces.fairy.tests

import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.fairy.Spartan
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

        val moves = game.getLegalMovesForPiece(whiteSpartan)

        print(moves)
        print(game.board)
    }
}