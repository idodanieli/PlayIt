package com.idodanieli.playit.games.chess.pieces.abilities

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.PieceWrapper
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.Drawer
import com.idodanieli.playit.games.chess.ui.TouchData
import com.idodanieli.playit.games.chess.ui.threat_visualizers.AvailableMovesVisualizer
import com.idodanieli.playit.games.chess.ui.threat_visualizers.TouchedSquareVisualizer

open class Bomber(piece: Piece): PieceWrapper(piece) {

    override fun visualize(touch: TouchData?, chessView: ChessView) {
        touch ?: return

        TouchedSquareVisualizer().visualize(touch, chessView)

        if (touch.isPreviewAbilityTouch()) {
            drawThreatenedSquares(chessView)
            return
        }

        AvailableMovesVisualizer().visualize(touch, chessView)
    }

    private fun drawThreatenedSquares(chessView: ChessView) {
        val squares = explosionThreatenedSquares(chessView.game.board)

        chessView.chessDrawer.drawSquares(squares, Drawer.COLOR_SINOPIA_LIGHT, Drawer.COLOR_SINOPIA_DARK)
    }

    private fun explosionThreatenedSquares(board: Board): List<Square> {
        return board.neighborSquares(this) + this.square
    }

    // --- General ---------------------------------------------------------------------------------
    fun getChild(): Piece {
        return piece
    }

    override fun copy(): Piece {
        return Bomber(piece.copy())
    }
}