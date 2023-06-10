package com.idodanieli.playit.games.chess.game_listener

import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.pieces.Piece

interface GameListener {
    fun onPieceCaptured(capturedPiece: Piece)
    fun onGameOver(winner: Player)
}