package com.idodanieli.playit.games.chess.ui.drawers

import android.content.Context
import android.graphics.RectF
import androidx.appcompat.R
import com.idodanieli.playit.games.chess.logic.BoardDimensions
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece

class ChessDrawer(
    dimensions: BoardDimensions,
    mode: String,
    context: Context,
    ) : PieceDrawer(context, mode, dimensions = dimensions) {

    var movingPiece: MovingPiece? = null

    private val lightColor = fetchColorFromAttribute(context, R.attr.colorAccent)
    private val darkColor =
        fetchColorFromAttribute(context, R.attr.colorPrimaryDark)

    fun drawChessboard() {
        val chessboardSquares = getChessboardSquares()
        drawSquares(chessboardSquares, lightColor, darkColor)
    }

    private fun getChessboardSquares(): List<Square> {
        return (0 until dimensions.cols).flatMap { col ->
            (0 until dimensions.rows).map { row ->
                Square(col, row)
            }
        }
    }

    fun drawSquares(squares: List<Square>, lightColor: Int, darkColor: Int) {
        for (square in squares) {
            drawSquareAccordingToHero(square, lightColor, darkColor)
        }
    }

    fun drawSquareAccordingToHero(square: Square, lightColor: Int, darkColor: Int) {
        val color = getSquareColor(square, lightColor, darkColor)
        drawSquareAccordingToHero(square, color)
    }

    fun drawSquareAccordingToHero(square: Square, color: Int) {
        // When the player is black the screen is flipped vertically so it's fitting to his perspective
        val squareAccordingToHero = if (hero.isBlack()) square.flipVertically(dimensions.rows) else square

        drawSquare(squareAccordingToHero, color)
    }

    private fun getSquareColor(square: Square, lightColor: Int, darkColor: Int): Int {
        if (isDarkSquare(square)) {
            return darkColor
        }

        return lightColor
    }

    private fun isDarkSquare(square: Square): Boolean {
        return (square.col + square.row) % 2 == 1
    }
}
