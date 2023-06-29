package com.idodanieli.playit.games.chess.pieces.abilities

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.PieceWrapper
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.ColorPallete
import com.idodanieli.playit.games.chess.ui.TouchData
import com.idodanieli.playit.games.chess.ui.threat_visualizers.AvailableMovesVisualizer
import com.idodanieli.playit.games.chess.ui.threat_visualizers.TouchedSquareVisualizer
import com.idodanieli.playit.games.chess.variants.Game

open class Bomber(piece: Piece): PieceWrapper(piece) {

    // --- Visualize Bomb --------------------------------------------------------------------------
    override fun visualize(touch: TouchData?, chessView: ChessView) {
        touch ?: return

        when(touch.touches) {
            TouchData.ACTIVATE_ABILITY_TOUCH_AMOUNT -> {
                return
            }
            TouchData.PREVIEW_ABILITY_TOUCH_AMOUNT -> {
                drawThreatenedSquares(chessView)
            }
            else -> {
                TouchedSquareVisualizer().visualize(touch, chessView)
                AvailableMovesVisualizer().visualize(touch, chessView)
            }
        }
    }

    private fun drawThreatenedSquares(chessView: ChessView) {
        val squares = explosionThreatenedSquares(chessView.game.board)

        chessView.chessDrawer.drawSquares(squares, ColorPallete.COLOR_SINOPIA_LIGHT, ColorPallete.COLOR_SINOPIA_DARK)
    }

    // --- Game Logic ------------------------------------------------------------------------------

    override fun hasAbility(): Boolean {
        return true
    }

    override fun applyAbility(game: Game) {
        val board = game.board

        for (square in explosionThreatenedSquares(board)) {
            board.pieceAt(square)?.let {
                game.applyCapture(capturingPiece = this,  capturedPiece =  it)
            }
        }
    }

    fun explosionThreatenedSquares(board: Board): List<Square> {
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