package com.idodanieli.playit.games.chess.pieces.tests

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Player
import com.idodanieli.playit.games.chess.Square
import com.idodanieli.playit.games.chess.pieces.Pawn
import org.junit.Test

class PawnTest {

    @Test
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

    @Test
    fun testEatMoveBlack() {
        val pawn = Pawn(Square(0, 6), Player.BLACK)
        val enemy = Pawn(Square(1, 5), Player.WHITE)

        val board = Board(mutableSetOf(pawn, enemy), 8)

        assert(enemy.square in pawn.eatMoves(board))

        board.pieces.remove(enemy)

        assert(enemy.square !in pawn.eatMoves(board)) {"The pawn at ${pawn.square} could eat " +
                "the enemy at ${enemy.square} even though its not in the board!"}
    }
}