package com.idodanieli.playit.games.chess.ui

import com.idodanieli.playit.games.chess.logic.Player

interface GameListener {
    fun onGameOver(winner: Player)
}