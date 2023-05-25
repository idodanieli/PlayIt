package com.idodanieli.playit.games.chess.ui

import android.content.Context
import android.graphics.*
import com.idodanieli.playit.games.chess.MODE_LOCAL
import com.idodanieli.playit.games.chess.MODE_ONLINE
import com.idodanieli.playit.games.chess.logic.Game
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece

val COLOR_TOUCHED = Color.parseColor("#CBC3E3")
val COLOR_LIGHT_AVAILABLE_SQUARE = Color.parseColor("#FF7276")
val COLOR_DARK_AVAILABLE_SQUARE = Color.parseColor("#E6676B")

private const val MOVING_PIECE_SCALE = 1.5f
private const val MOVING_PIECE_Y_OFFSET = 150f // so the user will see what piece hes moving

class ChessDrawer(private val size: Int, context: Context) : Drawer() {
    private var squareSize = 0f
    private var mode = MODE_ONLINE // TODO: Change this in the future

    private val lightColor = fetchColorFromAttribute(context, androidx.appcompat.R.attr.colorAccent)
    private val darkColor = fetchColorFromAttribute(context, androidx.appcompat.R.attr.colorPrimaryDark)

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
    private fun drawPiece(piece: Piece) {
        var pieceBitmap = getPieceBitmap(piece)!!

        // Flip the black players piece in local mode so it would be easier to play
        if (mode == MODE_LOCAL && piece.player == Player.BLACK) {
            pieceBitmap = flipBitmap(pieceBitmap, Direction.VERTICAL)!!
        }

        this.drawBitmapAtSquare(piece.square, pieceBitmap)
    }

    // drawBitmapAtPosition draws the piece in the given position on the screen
    // @param scale = the scaling of the piece
    private fun drawBitmapAtPosition(x: Float, y: Float, bitmap: Bitmap, scale: Float = 1f) =
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
    private fun drawBitmapAtSquare(square: Square, bitmap: Bitmap) =
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