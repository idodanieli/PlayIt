package com.idodanieli.playit.games.chess.ui

import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square

interface GameListener {
    fun onPieceMoved(origin: Square, dst: Square)
    fun onGameOver(winner: Player)
}