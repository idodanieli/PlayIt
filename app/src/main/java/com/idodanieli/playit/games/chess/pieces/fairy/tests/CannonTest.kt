package com.idodanieli.playit.games.chess.pieces.fairy.tests

import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.Bishop
import com.idodanieli.playit.games.chess.pieces.classic.King
import com.idodanieli.playit.games.chess.pieces.classic.Rook
import com.idodanieli.playit.games.chess.pieces.fairy.Cannon
import com.idodanieli.playit.games.chess.variants.ClassicGame
import org.junit.Test

class CannonTest {
    // C stands for white cannon
    // can capture the bishop or the rook at the first row
    // can move to the spots marked with x
    // . . . x . . . .
    // . . . x . . . .
    // . . . x . . . .
    // r b K C x x x x
    // . . . x . . . .
    // . . . k . . . .
    // . . . . . . . .
    // . . . r . . . .
    private val wCannon = Cannon(Square(3, 4), Player.WHITE)
    private val wKing = King(Square(2, 4), Player.WHITE)
    private val bBishop = Bishop(Square(1, 4), Player.BLACK)
    private val bRook1 = Rook(Square(0, 4), Player.BLACK)
    private val bRook2 = Rook(Square(3, 9), Player.BLACK)
    private val bKing = King(Square(3, 2), Player.BLACK)
    
    private val pieces = setOf(wCannon, wKing, bBishop, bRook1, bRook2, bKing)
    private val game = ClassicGame("", pieces, CHESSBOARD_SIZE)
    
    @Test
    fun testAvailableSquares() {
        
    }
    
    @Test
    fun testCapturableSquares() {
        
    }
}
