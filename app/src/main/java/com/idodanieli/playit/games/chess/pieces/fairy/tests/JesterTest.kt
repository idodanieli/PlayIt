package com.idodanieli.playit.games.chess.pieces.fairy.tests

import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.Pawn
import com.idodanieli.playit.games.chess.pieces.fairy.Jester
import org.junit.Test

class JesterTest {

    @Test
    fun testCreateMimickedPiece() {
        val jesterSquare = Square(7, 7)

        val jester = Jester(jesterSquare, Player.BLACK)
        val pawn = Pawn(Square(0, 0), Player.WHITE)
        val mimickedPiece = jester.createMimickedPiece(pawn)

        assert(mimickedPiece.square == jesterSquare)
        assert(mimickedPiece.player == Player.BLACK)
        assert(mimickedPiece.type == pawn.type)
    }
}