package com.idodanieli.playit.games.chess.ui.threat_visualizers

import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.ui.ChessDrawer
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.ColorPallete
import com.idodanieli.playit.games.chess.ui.TouchData


class TouchedSquareVisualizer: EventVisualizer {

    override fun visualize(chessView: ChessView) {
        chessView.focusedPiece ?: return

        val touch = chessView.focusedPiece!!

        drawTouch(touch, chessView.chessDrawer)
    }

    private fun drawTouch(touch: TouchData?, chessDrawer: ChessDrawer) {
        touch ?: return

        if (touch.isPreviewAbilityTouch()) {
            drawAbilitySquare(touch.square, chessDrawer)
        } else {
            drawTouchedSquare(touch.square, chessDrawer)
        }
    }

    private fun drawTouchedSquare(square: Square, chessDrawer: ChessDrawer) {
        chessDrawer.drawSquareAccordingToHero(square, ColorPallete.COLOR_TOUCHED)
    }

    private fun drawAbilitySquare(square: Square, chessDrawer: ChessDrawer) {
        chessDrawer.drawSquareAccordingToHero(square, ColorPallete.COLOR_ABILITY)
    }
}