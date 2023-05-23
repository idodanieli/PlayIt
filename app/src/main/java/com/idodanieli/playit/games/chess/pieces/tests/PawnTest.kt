package com.idodanieli.playit.games.chess.pieces.tests

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.Pawn
import org.junit.Test

class PawnTest {

    @Test // Tests that the pawn moves as expected ( basic movements )
    fun testWhiteMovements() {
        val pawn = Pawn(Square(0, 1), Player.WHITE)
        val board = Board(mutableSetOf(), 8)

        val moves = pawn.possibleMoves(board)

        assert(Square(0, 2) in moves) // Forward move
        assert(Square(0, 3) in moves) // Double-forward move
    }

    @Test // Tests that the pawn cant move forward while at the final rank
    fun testEighthRank() {
        val pawn = Pawn(Square(0, 7), Player.WHITE)

        val board = Board(mutableSetOf(), 8)

        val moves = pawn.possibleMoves(board)
        assert(moves.isEmpty())
    }

    @Test // Tests that the pawn cant eat out of the board's borders
    fun testEatMoveBlack() {
        val pawn = Pawn(Square(0, 6), Player.BLACK)
        val enemy = Pawn(Square(1, 5), Player.WHITE)

        val board = Board(mutableSetOf(pawn, enemy), 8)
        val moves = pawn.possibleMoves(board)

        assert(enemy.square in moves) { wrongMovesFormat(pawn, board, moves, "The black pawn could not eat the white pawn!") }

        board.remove(enemy)

        assert(enemy.square !in pawn.captureMoves(board)) {"The pawn at ${pawn.square} could eat " +
                "the enemy at ${enemy.square} even though its not in the board!"}
    }

    @Test // Tests that the pawn can leap over another piece when in the starting row
    fun testPawnCantLeap() {
        val pawn = Pawn(Square(0, 6), Player.BLACK)
        val enemy = Pawn(Square(0, 5), Player.WHITE)

        val board = Board(mutableSetOf(pawn, enemy), 8)
        val possibleMoves = pawn.possibleMoves(board)

        assert(possibleMoves.isEmpty()) { wrongMovesFormat(pawn, board, possibleMoves, "The pawn managed to leap over another piece when it shouldn't have") }
    }
}