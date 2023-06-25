package com.idodanieli.playit.games.chess.ui.threat_visualizers

import android.graphics.Color
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.ui.ChessDrawer
import com.idodanieli.playit.games.chess.ui.TouchData

class AvailableMovesVisualizer: TouchVisualizer {

    companion object {
        private val COLOR_LIGHT_AVAILABLE_SQUARE = Color.parseColor("#FF7276")
        private val COLOR_DARK_AVAILABLE_SQUARE = Color.parseColor("#E6676B")
    }

    override fun visualize(touch: TouchData?, chessDrawer: ChessDrawer) {
        touch ?: return

        drawAvailableMoves(chessDrawer, touch.availableMoves.keys)
    }

    private fun drawAvailableMoves(chessDrawer: ChessDrawer, moves: Set<Move>) {
        return drawAvailableSquares(chessDrawer, moves.map { it.dest })
    }

    private fun drawAvailableSquares(chessDrawer: ChessDrawer, squares: List<Square>) {
        chessDrawer.drawSquares(squares, COLOR_LIGHT_AVAILABLE_SQUARE, COLOR_DARK_AVAILABLE_SQUARE)
    }
}