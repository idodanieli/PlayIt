package com.idodanieli.playit.games.chess.ui.event_visualizers

import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.ui.drawers.ChessDrawer
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.utils.ColorPallete

class AvailableMovesVisualizer: EventVisualizer {

    override fun visualize(chessView: ChessView) {
        chessView.focusedPiece ?: return

        drawAvailableMoves(chessView.chessDrawer, chessView.focusedPiece!!.availableMoves.keys)
    }

    private fun drawAvailableMoves(chessDrawer: ChessDrawer, moves: Set<Move>) {
        return drawAvailableSquares(chessDrawer, moves.map { it.dest })
    }

    private fun drawAvailableSquares(chessDrawer: ChessDrawer, squares: List<Square>) {
        chessDrawer.drawSquares(squares, ColorPallete.COLOR_LIGHT_AVAILABLE_SQUARE, ColorPallete.COLOR_DARK_AVAILABLE_SQUARE)
    }
}