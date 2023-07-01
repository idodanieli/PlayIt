package com.idodanieli.playit.games.chess.game_subscriber

object LocalChessGameSubscriber: GameSubscriber {

    override fun onGameEvent(event: GameEvent) {
        when(event) {
            is GameSelectedEvent -> {
                event.chessView.publisher.notifySubscribers(
                    PlayersJoinedEvent("White", "Black")
                )

                event.chessView.startGame()
            }
        }
    }
}