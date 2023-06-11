package com.idodanieli.playit.games.chess.game_subscriber

import com.idodanieli.playit.games.chess.logic.GameEvent
import com.idodanieli.playit.games.chess.logic.GameSelectedEvent
import com.idodanieli.playit.games.chess.logic.GameSubscriber

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