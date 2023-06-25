package com.idodanieli.playit.games.chess.pieces.fairy

import android.graphics.Color
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.TouchData
import com.idodanieli.playit.games.chess.ui.threat_visualizers.AvailableMovesVisualizer
import com.idodanieli.playit.games.chess.ui.threat_visualizers.TouchedSquareVisualizer


class Terrorist(square: Square, player: Player) : BasePiece(square, player) {
    companion object {
        const val TYPE = "T"

        private val COLOR_SINOPIA = Color.parseColor("#D73502")
    }

    override val type = TYPE

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

        chessView.chessDrawer.drawSquares(squares, COLOR_SINOPIA, COLOR_SINOPIA)
    }

    override fun availableMoves(board: Board): List<Move> {
        return (availableSquares(board) + capturableSquares(board)).map { square -> Move(this.square, square) }
    }

    override fun availableSquares(board: Board): List<Square> {
        val squares = arrayListOf<Square>()

        squares += this.square + Square(1, 0)
        squares += this.square + Square(-1, 0)
        squares += this.square + Square(0, 1)
        squares += this.square + Square(0, -1)

        return squares.filter{it.inBorder(board.size)}
    }

    private fun explosionThreatenedSquares(board: Board): List<Square> {
        return board.neighborSquares(this) + this.square
    }
}