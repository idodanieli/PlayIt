package com.idodanieli.playit.games.chess.game_subscriber

interface IPublisher {
    fun subscribe(subscriber: GameSubscriber)
    fun subscribe(subscribers: Collection<GameSubscriber>)
    fun unsubscribe(subscriber: GameSubscriber)
    fun unsubscribeAll()
    fun notifySubscribers(event: GameEvent)
}

open class Publisher: IPublisher {
    private val subscribers = mutableListOf<GameSubscriber>()

    override fun subscribe(subscriber: GameSubscriber) {
        subscribers.add(subscriber)
    }
    override fun subscribe(subscribers: Collection<GameSubscriber>) {
        for (subscriber in subscribers) {
            subscribe(subscriber)
        }
    }

    override fun unsubscribe(subscriber: GameSubscriber) {
        subscribers.remove(subscriber)
    }

    override fun unsubscribeAll() {
        subscribers.clear()
    }

    override fun notifySubscribers(event: GameEvent) {
        for (subscriber in subscribers) {
            subscriber.onGameEvent(event)
        }
    }
}