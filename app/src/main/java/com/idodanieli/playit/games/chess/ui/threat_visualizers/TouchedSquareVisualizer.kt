package com.idodanieli.playit.games.chess.ui.threat_visualizers

import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.ui.ChessDrawer
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.Drawer
import com.idodanieli.playit.games.chess.ui.TouchData

class TouchedSquareVisualizer: TouchVisualizer {

    override fun visualize(touch: TouchData?, chessView: ChessView) {
        touch ?: return

        if (touch.isPreviewAbilityTouch()) {
            drawAbilitySquare(touch.square, chessView.chessDrawer)
        } else {
            drawTouchedSquare(touch.square, chessView.chessDrawer)
        }
    }

    private fun drawTouchedSquare(square: Square, chessDrawer: ChessDrawer) {
        chessDrawer.drawSquareAccordingToHero(square, Drawer.COLOR_TOUCHED)
    }

    private fun drawAbilitySquare(square: Square, chessDrawer: ChessDrawer) {
        chessDrawer.drawSquareAccordingToHero(square, Drawer.COLOR_ABILITY)
    }
}