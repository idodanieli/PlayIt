package com.idodanieli.playit.games.chess.logic.tests

import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.classic.*
import com.idodanieli.playit.games.chess.pieces.tests.errorFormat
import com.idodanieli.playit.games.chess.ui.CHESSBOARD_SIZE
import org.junit.Test

class GameTest {

    @Test
    fun testMovePiece() {
        val origin = Square(0, 1)
        val destination = Square(0, 3)

        val pawn = Pawn(origin, Player.BLACK)
        val enemyPawn = Pawn(destination, Player.WHITE)
        val game = Game(TEST_GAME_NAME, mutableSetOf(pawn, enemyPawn), CHESSBOARD_SIZE)

        game.movePiece(origin, destination)

        assert(game.board.pieceAt(destination) == pawn)
        assert(enemyPawn !in game.board.whitePieces)

        assert(origin !in game.board.map) { errorFormat(game.board, "origin square $origin still in board.map even after move") }
        assert(destination in game.board.map) { errorFormat(game.board, "dest square $destination not in board.map even after move") }
    }

    @Test
    fun testIsGameOver() {
        val bKing = King(Square(0, 0), Player.BLACK)
        val bPawn = Pawn(Square(0, 1), Player.BLACK)
        val wQueen = Queen(Square(2, 0), Player.WHITE)

        // Game is over - blacks king can't move ( checkmated by whites queen )
        // k . Q . . . . .
        // p . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        val finishedGame = Game(TEST_GAME_NAME, mutableSetOf(bKing, bPawn, wQueen), CHESSBOARD_SIZE)
        finishedGame.currentPlayer = Player.BLACK

        assert(finishedGame.isOver()) { errorFormat(finishedGame.board, "test returned that the game wasn't over even though it was ( queen checkmates king )") }

        // Game isn't over
        // k . . . . . . .
        // p . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // Q . . . . . . .
        val unthreateningWQueen = Queen(Square(0 , 7), Player.WHITE)
        val unfinishedGame = Game(TEST_GAME_NAME, mutableSetOf(bKing, bPawn, unthreateningWQueen), CHESSBOARD_SIZE)
        unfinishedGame.currentPlayer = Player.BLACK

        assert(!unfinishedGame.isOver()) { errorFormat(unfinishedGame.board, "test returned that the game was over even though it wasn't") }
    }

    companion object {
        private const val TEST_GAME_NAME = "test"
    }
}