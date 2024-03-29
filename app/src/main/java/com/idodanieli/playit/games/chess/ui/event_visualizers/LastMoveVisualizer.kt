package com.idodanieli.playit.games.chess.ui.event_visualizers

import com.idodanieli.playit.games.chess.game_subscriber.GameEvent
import com.idodanieli.playit.games.chess.game_subscriber.GameSubscriber
import com.idodanieli.playit.games.chess.game_subscriber.MoveEvent
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.ui.views.ChessView
import com.idodanieli.playit.games.chess.ui.utils.ColorPallete
import com.idodanieli.playit.games.chess.ui.utils.Clearable

class LastMoveVisualizer: EventVisualizer, GameSubscriber, Clearable {
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
            visualizeMove(chessView, it)

            for (move in it.followUpMoves) {
                visualizeMove(chessView, move)
            }
        }
    }

    private fun visualizeMove(chessView: ChessView, move: Move) {
        drawSquare(chessView, move.origin)
        drawSquare(chessView, move.dest)
    }

    private fun drawSquare(chessView: ChessView, square: Square) {
        chessView.chessDrawer.drawSquareAccordingToHero(square, ColorPallete.COLOR_LAST_MOVE_LIGHT, ColorPallete.COLOR_LAST_MOVE_DARK)
    }

    override fun clear() {
        lastMove = null
    }
}