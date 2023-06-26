package com.idodanieli.playit.games.chess.game_subscriber

object LocalChessGameSubscriber: GameSubscriber {

    override fun onGameEvent(event: GameEvent) {
        when(event) {
            is GameSelectedEvent -> {
                event.chessView.setPlayers("White", "Black")
                event.chessView.startGame()
            }
        }
    }
}