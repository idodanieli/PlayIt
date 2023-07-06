package com.idodanieli.playit.games.chess.pieces.fairy.tests

import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.King
import com.idodanieli.playit.games.chess.pieces.classic.Pawn
import com.idodanieli.playit.games.chess.pieces.fairy.Grasshopper
import com.idodanieli.playit.games.chess.pieces.tests.errorFormat
import com.idodanieli.playit.games.chess.pieces.tests.listsContainSameValues
import com.idodanieli.playit.games.chess.variants.ClassicGame
import org.junit.Test
import kotlin.math.exp

class GrasshopperTest {

    // H marks Grasshopper
    // X marks the spots the grasshopper can jump to
    // . . . . . . . X
    // p . . X . . k .
    // . p . P . . . .
    // . . . . . . . .
    // . . . H P P . .
    // . . P . . . . .
    // . X . K . . . .
    // . . . X . . . .
    @Test
    fun testAvailableMoves() {
        val wGrasshopper = Grasshopper(Square(3, 3), Player.WHITE)

        val bPawn1 = Pawn(Square(0, 6), Player.BLACK)
        val bKing = King(Square(6, 6), Player.BLACK)
        val bPawn2 = Pawn(Square(1, 5), Player.BLACK)
        val wPawn1 = Pawn(Square(3, 5), Player.WHITE)
        val wPawn2 = Pawn(Square(4, 3), Player.WHITE)
        val wPawn3 = Pawn(Square(5, 3), Player.WHITE)
        val wPawn4 = Pawn(Square(2, 2), Player.WHITE)
        val wKing = King(Square(3, 1), Player.WHITE)

        val pieces = setOf(bPawn1, bPawn2, wPawn1, wPawn2, wPawn3, wPawn4, wKing, bKing, wGrasshopper)
        val game = ClassicGame("", pieces, CHESSBOARD_SIZE)

        val expectedSquares = listOf(
            Square(7, 7),
            Square(3, 6),
            Square(1, 1),
            Square(3, 0)
        )

        val squares = wGrasshopper.availableSquares(game.board)

        assert( listsContainSameValues(squares, expectedSquares) ) {
            errorFormat(game.board, "hopper should be able to move to ${expectedSquares}," +
                    " instead the result was: $squares")
        }
    }
}