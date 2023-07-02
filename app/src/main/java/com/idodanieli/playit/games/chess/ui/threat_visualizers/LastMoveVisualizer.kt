package com.idodanieli.playit.games.chess.ui.threat_visualizers

import com.idodanieli.playit.games.chess.game_subscriber.GameEvent
import com.idodanieli.playit.games.chess.game_subscriber.GameSubscriber
import com.idodanieli.playit.games.chess.game_subscriber.MoveEvent
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.ColorPallete

class LastMoveVisualizer: EventVisualizer, GameSubscriber {
    private var lastMove: Move? = null

    override fun onGameEvent(event: GameEvent) {
        when(event) {
            is MoveEvent -> {
                lastMove = event.move
            }
        }
    }

    override fun visualize(chessView: ChessView) {
        lastMove?.let {
            drawSquare(chessView, it.origin)
            drawSquare(chessView, it.dest)
        }
    }

    private fun drawSquare(chessView: ChessView, square: Square) {
        chessView.chessDrawer.drawSquareAccordingToHero(square, ColorPallete.COLOR_LAST_MOVE_LIGHT, ColorPallete.COLOR_LAST_MOVE_DARK)
    }
}