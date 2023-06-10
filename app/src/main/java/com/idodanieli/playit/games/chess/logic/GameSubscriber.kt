package com.idodanieli.playit.games.chess.logic

interface GameSubscriber {
    fun onGameEvent(event: GameEvent)
}