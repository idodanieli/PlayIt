package com.idodanieli.playit.games.chess.pieces.tests

import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.PieceGuide
import com.idodanieli.playit.games.chess.pieces.fairy.Terrorist
import org.junit.Test

class PieceGuideTest {

    @Test
    fun testPieceGuide() {
        val terrorist = Terrorist(Square(0, 0), Player.WHITE)
        val desc = PieceGuide.explain(terrorist)
        print(desc)
    }
}