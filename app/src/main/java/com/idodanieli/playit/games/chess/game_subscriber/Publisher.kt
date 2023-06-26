package com.idodanieli.playit.games.chess.game_subscriber

import com.idodanieli.playit.games.chess.logic.GameEvent

open class Publisher {
    private val subscribers = mutableListOf<GameSubscriber>()

    fun subscribe(subscriber: GameSubscriber) {
        subscribers.add(subscriber)
    }
    fun subscribe(subscribers: Collection<GameSubscriber>) {
        for (subscriber in subscribers) {
            subscribe(subscriber)
        }
    }

    fun unsubscribe(subscriber: GameSubscriber) {
        subscribers.remove(subscriber)
    }

    fun unsubscribeAll() {
        subscribers.clear()
    }

    fun notifySubscribers(event: GameEvent) {
        for (subscriber in subscribers) {
            subscriber.onGameEvent(event)
        }
    }
}