package com.idodanieli.playit.games.chess.ui.threat_visualizers

import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.ui.ChessDrawer
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.ColorPallete

class TouchedSquareVisualizer: EventVisualizer {

    override fun visualize(chessView: ChessView) {
        chessView.currentTouch ?: return

        val touch = chessView.currentTouch!!

        if (touch.isPreviewAbilityTouch()) {
            drawAbilitySquare(touch.square, chessView.chessDrawer)
        } else {
            drawTouchedSquare(touch.square, chessView.chessDrawer)
        }
    }

    private fun drawTouchedSquare(square: Square, chessDrawer: ChessDrawer) {
        chessDrawer.drawSquareAccordingToHero(square, ColorPallete.COLOR_TOUCHED)
    }

    private fun drawAbilitySquare(square: Square, chessDrawer: ChessDrawer) {
        chessDrawer.drawSquareAccordingToHero(square, ColorPallete.COLOR_ABILITY)
    }
}