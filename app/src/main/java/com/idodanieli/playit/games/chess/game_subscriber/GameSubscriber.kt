package com.idodanieli.playit.games.chess.game_subscriber

interface GameSubscriber {
    fun onGameEvent(event: GameEvent)
}