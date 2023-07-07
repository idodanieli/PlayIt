package com.idodanieli.playit.games.chess.logic.tests

import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.classic.*
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.tests.errorFormat
import com.idodanieli.playit.games.chess.variants.ClassicGame
import org.junit.Test

class PinningTest {
    private val blackKing = King(Square(4, 7), Player.BLACK)
    private val blackPawn = Pawn(Square(3, 6), Player.BLACK)
    private val whiteBishop = Bishop(Square(2, 5), Player.WHITE)
    private val pieces = mutableSetOf<Piece>(blackKing, blackPawn, whiteBishop)

    private val game = ClassicGame("", pieces, CHESSBOARD_SIZE, CHESSBOARD_SIZE)

    @Test // that a piece can capture it's pinner
    fun testCanCapturePinner() {
        // . . . . k . . .
        // . . . p . . . .
        // . . B . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .

        game.currentPlayer = Player.BLACK

        val blackPawnDestinations = game.getLegalMovesForPiece(blackPawn).map { it.dest }

        assert(whiteBishop.square in blackPawnDestinations) {
            errorFormat(game.board, "$blackPawn could not eat $whiteBishop")
        }

        assert(blackPawnDestinations.size == 1) {
            errorFormat(game.board, "$blackPawn could move to other places while being pinned! ( $blackPawnDestinations )")
        }
    }

    @Test // that a piece cant move while its being pinned
    fun testCantMoveWhilePinned() {
        // 7 . . . . k . . .
        // 6 . . . p . . . .
        // 5 . . . . . . . .
        // 4 . B . . . . . .
        // 3 . . . . . . . .
        // 2 . . . . . . . .
        // 1 . . . . . . . .
        // 0 . . . . . . . .
        //   0 1 2 3 4 5 6 7

        game.currentPlayer = Player.BLACK
        game.board.move(whiteBishop, Square(1, 4))

        val blackPawnMoves = game.getLegalMovesForPiece(blackPawn)

        assert(blackPawnMoves.isEmpty()) {
            errorFormat(game.board, "$blackPawn can move while being pinned! ( $blackPawnMoves )")
        }
    }
}