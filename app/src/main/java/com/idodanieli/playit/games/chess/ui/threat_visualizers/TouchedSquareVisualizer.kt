package com.idodanieli.playit.games.chess.ui.threat_visualizers

import android.graphics.Color
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.ui.ChessDrawer
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.TouchData

class TouchedSquareVisualizer: TouchVisualizer {

    companion object {
        private val COLOR_TOUCHED = Color.parseColor("#CBC3E3") // Light Purple
        private val COLOR_ABILITY = Color.parseColor("#FFD700") // Gold
    }

    override fun visualize(touch: TouchData?, chessView: ChessView) {
        touch ?: return

        if (touch.isPreviewAbilityTouch()) {
            drawAbilitySquare(touch.square, chessView.chessDrawer)
        } else {
            drawTouchedSquare(touch.square, chessView.chessDrawer)
        }
    }

    private fun drawTouchedSquare(square: Square, chessDrawer: ChessDrawer) {
        chessDrawer.drawSquareAccordingToHero(square, COLOR_TOUCHED)
    }

    private fun drawAbilitySquare(square: Square, chessDrawer: ChessDrawer) {
        chessDrawer.drawSquareAccordingToHero(square, COLOR_ABILITY)
    }
}