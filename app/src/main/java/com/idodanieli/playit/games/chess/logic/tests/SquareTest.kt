package com.idodanieli.playit.games.chess.logic.tests

import com.idodanieli.playit.games.chess.logic.Square
import org.junit.Test

class SquareTest {

    @Test
    fun testFlipVertically() {
        assert(Square(0, 2).flipVertically(8) == Square(0, 5))
        assert(Square(3, 0).flipVertically(8) == Square(3, 7))
        assert(Square(7, 7).flipVertically(8) == Square(7, 0))
    }
}