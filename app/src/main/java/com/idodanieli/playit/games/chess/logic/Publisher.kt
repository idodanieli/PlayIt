package com.idodanieli.playit.games.chess.logic

open class Publisher {
    private val subscribers = mutableListOf<GameSubscriber>()

    fun subscribe(subscriber: GameSubscriber) {
        subscribers.add(subscriber)
    }

    fun unsubscribe(subscriber: GameSubscriber) {
        subscribers.remove(subscriber)
    }

    fun notifySubscribers(event: GameEvent) {
        for (subscriber in subscribers) {
            subscriber.onGameEvent(event)
        }
    }
}