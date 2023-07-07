package com.idodanieli.playit.games.chess.pieces.tests

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.DEFAULT_DIMENSIONS
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.King
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.pieces.classic.Queen
import com.idodanieli.playit.games.chess.pieces.classic.Rook
import com.idodanieli.playit.games.chess.variants.ClassicGame
import org.junit.Test

class KingTest {
    private val king = King(Square(4, 7), Player.WHITE)
    private val leftRook = Rook(Square(0, 7), Player.WHITE)
    private val rightRook = Rook(Square(7, 7), Player.WHITE)
    private var board = Board(mutableSetOf(king, leftRook, rightRook), DEFAULT_DIMENSIONS)

    @Test // that the getCastlingMoves function works
    fun testCastling() {
        val tmpBoard = board.copy()
        val game = ClassicGame("", board.pieces(), board.dimensions.cols, board.dimensions.rows) // TODO: Move name out of game... it's annoying

        val blockingKnight = Knight(Square(6, 7), Player.WHITE)
        game.board.add(blockingKnight)

        val castlingMoves = king.getCastlingMoves(game.board)
        assert(castlingMoves.size == 1) { errorFormat(game.board, "function returned ${castlingMoves.size} castling moves instead of 1") }

        val expectedKingDest = Square(2, 7)
        val expectedRookDest = Square(3, 7)
        game.applyMove(castlingMoves[0])

        assert(king.square == expectedKingDest) {
            errorFormat(game.board, "king should have been at $expectedKingDest instead of ${king.square} after the castling")
        }

        assert(leftRook.square == expectedRookDest) {
            errorFormat(game.board, "rook should have been at $expectedRookDest instead of ${leftRook.square} after the castling")
        }

        board = tmpBoard
    }

    @Test // that the king can castle with the right rook ( not moved, nothing blocking or threatening )
    fun testCanCastleHappyFlow() {
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // R . . . K . . R
        assert(king.canCastleWith(rightRook, board)) { errorFormat(board, "King could NOT castle with unmoved right rook") }
    }

    @Test // that the castleWith function works for moved and unmoved rooks
    fun testCanCastleWithMovedRook() {
        leftRook.moved = true

        assert(!king.canCastleWith(leftRook, board)) { errorFormat(board, "King could castle with MOVED left rook") }

        leftRook.moved = false
    }

    @Test // that the king cant castle with another piece blocking the way
    fun testCanCastleWithBlockingPieceInTheWay() {
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // R . . . K . N R
        val blockingKnight = Knight(Square(6, 7), Player.WHITE)
        board.add(blockingKnight)

        assert(!king.canCastleWith(rightRook, board)) {
            errorFormat(board, "King could castle with right rook even though it's blocked by the knight")
        }

        board.remove(blockingKnight)
    }

    @Test // that the king cant castle with a square threatened by an enemy piece on the way
    fun testCanCastleWithThreateningSquareOnTheWay() {
        // . . . . . . q .
        // . . . . . . | .
        // . . . . . . | .
        // . . . . . . | .
        // . . . . . . | .
        // . . . . . . | .
        // . . . . . . | .
        // R . . . K . x R
        val threateningQueen = Queen(Square(6, 0), Player.BLACK)
        board.add(threateningQueen)

        assert(!king.canCastleWith(rightRook, board)) {
            errorFormat(board, "king could castle with right rook even though a square between" +
                    "them is threatened by the enemy's queen!")
        }

        board.remove(threateningQueen)
    }
}