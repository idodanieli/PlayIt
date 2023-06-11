package com.idodanieli.playit.games.chess.logic.tests

import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.classic.*
import com.idodanieli.playit.games.chess.pieces.tests.errorFormat
import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.pieces.fairy.Empress
import org.junit.Test

class GameTest {

    @Test
    fun testMovePiece() {
        val origin = Square(0, 1)
        val destination = Square(0, 3)

        val pawn = Pawn(origin, Player.BLACK)
        val enemyPawn = Pawn(destination, Player.WHITE)
        val game = Game(TEST_GAME_NAME, mutableSetOf(pawn, enemyPawn), CHESSBOARD_SIZE)

        val move = Move(origin, destination)
        game.applyMove(move)

        assert(game.board.pieceAt(destination) == pawn)
        assert(enemyPawn !in game.board.whitePieces)

        assert(origin !in game.board.map) {
            errorFormat(
                game.board,
                "origin square $origin still in board.map even after move"
            )
        }
        assert(destination in game.board.map) {
            errorFormat(
                game.board,
                "dest square $destination not in board.map even after move"
            )
        }
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

        assert(finishedGame.isOver()) {
            errorFormat(
                finishedGame.board,
                "test returned that the game wasn't over even though it was ( queen checkmates king )"
            )
        }

        // Game isn't over
        // k . . . . . . .
        // p . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // Q . . . . . . .
        val unthreateningWQueen = Queen(Square(0, 7), Player.WHITE)
        val unfinishedGame =
            Game(TEST_GAME_NAME, mutableSetOf(bKing, bPawn, unthreateningWQueen), CHESSBOARD_SIZE)
        unfinishedGame.currentPlayer = Player.BLACK

        assert(!unfinishedGame.isOver()) {
            errorFormat(
                unfinishedGame.board,
                "test returned that the game was over even though it wasn't"
            )
        }
    }

    // These are used for testFilterBlockingMoves and testCanMove
    private val bBishopOriginSquare = Square(2, 0)

    private val bKing = King(Square(0, 0), Player.BLACK)
    private val bBishop = Bishop(bBishopOriginSquare, Player.BLACK)
    private val bPawn = Pawn(Square(6, 1), Player.BLACK)
    private val wQueen = Queen(Square(2, 2), Player.WHITE)

    // Bishop could move to (1, 1) to block the check
    // k . b . . . . .
    // . . . . . . p .
    // . . Q . . . . .
    // . . . . . . . .
    // . . . . . . . .
    // . . . . . . . .
    // . . . . . . . .
    // . . . . . . . .
    private val game =
        Game(TEST_GAME_NAME, mutableSetOf(bKing, bBishop, bPawn, wQueen), CHESSBOARD_SIZE)

    @Test
    fun testFilterBlockingMoves() {
        assert(game.getLegalMovesForPiece(bBishop).size == 1)
        {
            errorFormat(
                game.board,
                "test result concluded that $bBishop couldn't block the check even though it could"
            )
        }

        assert(bBishop.square == bBishopOriginSquare) { "bishops origin square change after test from: $bBishopOriginSquare to ${bBishop.square}" }

        // Bishop could NOT move to (1, 1) to block the check
        // k . b . . . . .
        // . . . . . . . .
        // R . Q . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        val wRook = Rook(Square(0, 1), Player.WHITE)
        val game2 =
            Game(TEST_GAME_NAME, mutableSetOf(bKing, bBishop, wQueen, wRook), CHESSBOARD_SIZE)
        val blockingMoves = game2.getLegalMovesForPiece(bBishop)

        assert(blockingMoves.isEmpty())
        {
            errorFormat(
                game2.board,
                "test result concluded that $blockingMoves could block the check even though it couldn't"
            )
        }
    }

    @Test // Tests that a piece can only do check-blocking moves while the player is checked
    fun testGetLegalMovesForPiece() {
        // . . . m k . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . N . . .
        // . . . . M . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        val bKing = King(Square(4, 7), Player.BLACK)
        val bEmpress = Empress(Square(3, 7), Player.BLACK)
        val wEmpress = Empress(Square(4, 3), Player.WHITE)
        val wKnight = Knight(Square(4, 4), Player.WHITE)

        val game = Game("", setOf(bKing, bEmpress, wEmpress, wKnight), CHESSBOARD_SIZE)
        val wKnightMove = Move(Square(4, 4), Square(2, 5))

        // . . . m k . . .
        // . . . . . . . .
        // . . N . . . . .
        // . . . . . . . .
        // . . . . M . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        game.applyMove(wKnightMove)

        val bEmpressLegalMoves = game.getLegalMovesForPiece(bEmpress)

        assert(bEmpressLegalMoves.size == 1) {
            errorFormat(game.board, "$bEmpress could move to $bEmpressLegalMoves while the player is checked!")
        }
    }

    @Test // Tests that you cant apply a move on an illegal move
    fun applyMoveOnIllegalMove() {
        val move = Move(
            Square(0, 0),
            Square(9, 9),
        )

        game.applyMove(move)
    }

    companion object {
        private const val TEST_GAME_NAME = "test"
    }
}