package com.idodanieli.playit.games.chess.ui.threat_visualizers

import com.idodanieli.playit.games.chess.game_subscriber.GameEvent
import com.idodanieli.playit.games.chess.game_subscriber.GameSubscriber
import com.idodanieli.playit.games.chess.game_subscriber.MoveEvent
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.ColorPallete
import com.idodanieli.playit.games.chess.ui.TouchData

// TODO: This breaks design. TouchVisualizer -> EventVisualizer
// TODO: Will work with events like every single component

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
            chessView.chessDrawer.drawSquareAccordingToHero(it.origin, ColorPallete.COLOR_YELLOW_MARKER)
            chessView.chessDrawer.drawSquareAccordingToHero(it.dest, ColorPallete.COLOR_GREEN_MARKER)
        }
    }
}