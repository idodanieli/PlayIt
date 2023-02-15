package com.idodanieli.playit.games.chess

import android.graphics.*
import android.util.Log
import com.idodanieli.playit.games.chess.pieces.Piece

val COLOR_LIGHT = Color.parseColor("#ffe9c5")
val COLOR_DARK = Color.parseColor("#855E42")
val COLOR_TOUCHED = Color.parseColor("#CBC3E3")
val COLOR_LIGHT_AVAILABLE_SQUARE = Color.parseColor("#FF7276")
val COLOR_DARK_AVAILABLE_SQUARE = Color.parseColor("#E6676B")

private const val MOVING_PIECE_SCALE = 1.5f
private const val MOVING_PIECE_Y_OFFSET = 150f // so the user will see what piece hes moving

class ChessDrawer(private val size: Int, private val lightColor: Int, private val darkColor: Int) {
    private var canvas = Canvas()
    private var squareSize = 0f

    fun setCanvas(canvas: Canvas) {
        this.canvas = canvas
    }

    fun setSize(size: Float) {
        this.squareSize = size
    }

    // drawPieces draws all the pieces on the game.board
    // @movingPiece: the piece currently touched by the user. will be drawn on the touched position and not at any specific square
    fun drawPieces(game: Game, movingPiece: MovingPiece?) {
        game.pieces().forEach { piece ->
            if (movingPiece == null || piece != movingPiece.piece) {
                this.drawPiece(piece)
            }
        }

        movingPiece?.let {
            this.drawBitmapAtPosition(
                it.x,
                it.y - MOVING_PIECE_Y_OFFSET,
                it.bitmap,
                scale = MOVING_PIECE_SCALE
            )
        }
    }

    // drawPiece draws the given piece at the right square on the game.board
    fun drawPiece(piece: Piece) {
        this.drawBitmapAtSquare(piece.square, getPieceBitmap(piece)!!)
    }

    // drawPieceAtPosition draws the piece in the given position on the screen
    // @param scale = the scaling of the piece
    fun drawBitmapAtPosition(x: Float, y: Float, bitmap: Bitmap, scale: Float = 1f) =
        canvas.drawBitmap(
            bitmap,
            null,
            RectF(
                x - squareSize * scale / 2,
                y - squareSize * scale / 2,
                x + squareSize * scale / 2,
                y + squareSize * scale / 2
            ),
            Paint()
        )

    // drawSquare draws a square
    fun drawBitmapAtSquare(square: Square, bitmap: Bitmap) =
        this.canvas.drawBitmap(
            bitmap,
            null,
            RectF(
                square.col * squareSize,
                (size - 1 - square.row) * squareSize,
                (square.col + 1) * squareSize,
                ((size - 1 - square.row) + 1) * squareSize
            ),
            Paint()
        )

    // drawChessboard.board draws the whole chessboard ( without the pieces )
    fun drawChessboard() {
        for (row in 0 until size) {
            for (col in 0 until size) {
                val square = Square(col, row)
                this.drawSquare(square, if (square.isDark()) darkColor else lightColor)
            }
        }
    }

    fun drawSquares(squares: List<Square>, lightColor: Int, darkColor: Int) {
        for (square in squares) {
            this.drawSquare(square, if (square.isDark()) darkColor else lightColor)
        }
    }

    // drawSquare draws the square at the right position on the screen
    fun drawSquare(square: Square, color: Int) {
        this.canvas.drawRect(
            square.col * squareSize,
            this.canvas.height - square.row * squareSize,
            (square.col + 1) * squareSize,
            this.canvas.height - (square.row + 1) * squareSize,
            getPaint(color)
        )
    }
}

// getPaint returns a paint with the given color
private fun getPaint(color: Int): Paint {
    val paint = Paint()
    paint.color = color

    return paint
}