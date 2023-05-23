package com.idodanieli.playit.games.chess.logic.tests

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Game
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.Pawn
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
        val game = Game("test", mutableSetOf(pawn, enemyPawn), CHESSBOARD_SIZE)

        game.movePiece(origin, destination)

        assert(game.board.pieceAt(destination) == pawn)
        assert(enemyPawn !in game.board.whitePieces)

        assert(origin !in game.board.map) { errorFormat(game.board, "origin square $origin still in board.map even after move") }
        assert(destination in game.board.map) { errorFormat(game.board, "dest square $destination not in board.map even after move") }
    }
}