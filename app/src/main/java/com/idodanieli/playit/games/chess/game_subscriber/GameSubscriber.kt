package com.idodanieli.playit.games.chess.game_subscriber

import com.idodanieli.playit.games.chess.logic.GameEvent

interface GameSubscriber {
    fun onGameEvent(event: GameEvent)
}