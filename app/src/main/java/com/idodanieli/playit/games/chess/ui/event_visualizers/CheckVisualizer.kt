package com.idodanieli.playit.games.chess.ui.event_visualizers

import com.idodanieli.playit.games.chess.game_subscriber.CheckEvent
import com.idodanieli.playit.games.chess.game_subscriber.GameEvent
import com.idodanieli.playit.games.chess.game_subscriber.GameSubscriber
import com.idodanieli.playit.games.chess.game_subscriber.MoveEvent
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.TYPE_KING
import com.idodanieli.playit.games.chess.ui.views.ChessView
import com.idodanieli.playit.games.chess.ui.drawers.Drawer

class CheckVisualizer : EventVisualizer, GameSubscriber {
    private var checkedPiece: Piece? = null

    override fun onGameEvent(event: GameEvent) {
        when (event) {
            is CheckEvent -> {
                checkedPiece = event.game.board.getPiece(TYPE_KING, event.checkedPlayer)
            }

            is MoveEvent -> {
                checkedPiece = null
            }
        }
    }

    override fun visualize(chessView: ChessView) {
        checkedPiece?.let {
            chessView.chessDrawer.drawBitmapAtSquareAccordingToHero(
                Drawer.RED_GLOW_INDICATOR,
                it.square,
                chessView.hero
            )
        }
    }
}