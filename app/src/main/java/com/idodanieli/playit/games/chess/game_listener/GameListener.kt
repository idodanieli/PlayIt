package com.idodanieli.playit.games.chess.game_listener

import com.idodanieli.playit.games.chess.logic.Player

interface GameListener {
    fun onGameOver(winner: Player)
}