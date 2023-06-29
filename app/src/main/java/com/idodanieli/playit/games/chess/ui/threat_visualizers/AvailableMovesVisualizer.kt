package com.idodanieli.playit.games.chess.ui.threat_visualizers

import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.ui.ChessDrawer
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.ColorPallete
import com.idodanieli.playit.games.chess.ui.TouchData

class AvailableMovesVisualizer: TouchVisualizer {

    override fun visualize(touch: TouchData?, chessView: ChessView) {
        touch ?: return

        drawAvailableMoves(chessView.chessDrawer, touch.availableMoves.keys)
    }

    private fun drawAvailableMoves(chessDrawer: ChessDrawer, moves: Set<Move>) {
        return drawAvailableSquares(chessDrawer, moves.map { it.dest })
    }

    private fun drawAvailableSquares(chessDrawer: ChessDrawer, squares: List<Square>) {
        chessDrawer.drawSquares(squares, ColorPallete.COLOR_LIGHT_AVAILABLE_SQUARE, ColorPallete.COLOR_DARK_AVAILABLE_SQUARE)
    }
}