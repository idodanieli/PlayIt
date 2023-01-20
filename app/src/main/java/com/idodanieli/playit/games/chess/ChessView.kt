package com.idodanieli.playit.games.chess

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.idodanieli.playit.R
import kotlin.math.min

private const val CHESS_BOARD_SIZE = 8
private const val MOVING_PIECE_SCALE = 1.5f
private const val MOVING_PIECE_Y_OFFSET = 150f // so the user will see what piece hes moving

private var BITMAPS: MutableMap<Player, MutableMap<Type, Bitmap>> = mutableMapOf()
private val COLOR_LIGHT = Color.parseColor("#ffe9c5")
private val COLOR_DARK = Color.parseColor("#855E42")
private val COLOR_TOUCHED = Color.parseColor("#CBC3E3")


class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val chessDrawer = ChessDrawer(COLOR_LIGHT, COLOR_DARK)
    private var squareSize = 0f
    private var movingPiece: MovingPiece? = null
    private var previousTouchedSquare: Square = Square(-1, -1)
    private var currentlyTouchedSquare: Square? = null
    private var board: Board? = Board()

    init {
        loadBitmaps(resources)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(smaller, smaller)
    }

    // onDraw is called everytime invalidate() is called
    // the order of the draw functions inside is crucial
    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        squareSize = width / CHESS_BOARD_SIZE.toFloat()

        chessDrawer.setCanvas(canvas)
        chessDrawer.setSize(squareSize)

        chessDrawer.drawChessboard()

        currentlyTouchedSquare?.let { // if a square is touched, it will be highlighted purple for indication
            chessDrawer.drawSquare(it, COLOR_TOUCHED)
        }

        chessDrawer.drawPieces(board, movingPiece)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        val touchedSquare = getTouchedSquare(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                previousTouchedSquare = touchedSquare
                currentlyTouchedSquare = touchedSquare

                board?.pieceAt(touchedSquare)?.let {
                    movingPiece = MovingPiece(it, event.x, event.y, getPieceBitmap(it)!!)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                currentlyTouchedSquare = touchedSquare
                movingPiece?.let {
                    it.x = event.x
                    it.y = event.y
                }

                invalidate() // calls onDraw
            }
            MotionEvent.ACTION_UP -> {
                if (previousTouchedSquare.col != touchedSquare.col || previousTouchedSquare.row != touchedSquare.row) {
                    if (board?.canMove(previousTouchedSquare, touchedSquare) == true) {
                        board?.movePiece(previousTouchedSquare, touchedSquare)
                    }

                }
                movingPiece = null
                currentlyTouchedSquare = null
                invalidate() // calls onDraw
            }
        }
        return true
    }

    // getSquareTouched returns the square touched by the position in the MotionEvent
    private fun getTouchedSquare(event: MotionEvent): Square {
        val touchedColumn = (event.x / squareSize).toInt()
        val touchedRow = 7 - (event.y / squareSize).toInt()

        return Square(touchedColumn, touchedRow)
    }
}

private class ChessDrawer(private val lightColor: Int, private val darkColor: Int) {
    private var canvas = Canvas()
    private var squareSize = 0f

    fun setCanvas(canvas: Canvas) {
        this.canvas = canvas
    }

    fun setSize(size: Float) {
        this.squareSize = size
    }

    // drawPieces draws all the pieces on the board
    // @movingPiece: the piece currently touched by the user. will be drawn on the touched position and not at any specific square
    fun drawPieces(board: Board?, movingPiece: MovingPiece?) {
        board?.pieces()?.forEach { piece ->
            if (movingPiece == null || piece != movingPiece.piece) {
                this.drawPiece(piece)
            }
        }

        movingPiece?.let {
            this.drawBitmapAtPosition(it.x, it.y - MOVING_PIECE_Y_OFFSET, it.bitmap, scale = MOVING_PIECE_SCALE)
        }
    }

    // drawPiece draws the given piece at the right square on the board
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
                (7 - square.row) * squareSize,
                (square.col + 1) * squareSize,
                ((7 - square.row) + 1) * squareSize
            ),
            Paint()
        )

    // drawChessboard draws the whole chessboard ( without the pieces )
    fun drawChessboard() {
        for (row in 0 until CHESS_BOARD_SIZE) {
            for (col in 0 until CHESS_BOARD_SIZE) {
                val square = Square(col, row)
                val isDark = (square.col + square.row) % 2 == 1
                this.drawSquare(square, if (isDark) darkColor else lightColor)
            }
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

private data class MovingPiece(val piece: Piece, var x: Float, var y: Float, val bitmap: Bitmap)

//// HELPER FUNCTIONS \\\\

private fun loadBitmaps(resources: Resources) {
    BITMAPS[Player.WHITE] = mutableMapOf(
        Type.KING to BitmapFactory.decodeResource(resources, R.drawable.king_white),
        Type.QUEEN to BitmapFactory.decodeResource(resources, R.drawable.queen_white),
        Type.ROOK to BitmapFactory.decodeResource(resources, R.drawable.rook_white),
        Type.BISHOP to BitmapFactory.decodeResource(resources, R.drawable.bishop_white),
        Type.KNIGHT to BitmapFactory.decodeResource(resources, R.drawable.knight_white),
        Type.PAWN to BitmapFactory.decodeResource(resources, R.drawable.pawn_white)
    )

    BITMAPS[Player.BLACK] = mutableMapOf(
        Type.KING to BitmapFactory.decodeResource(resources, R.drawable.king_black),
        Type.QUEEN to BitmapFactory.decodeResource(resources, R.drawable.queen_black),
        Type.ROOK to BitmapFactory.decodeResource(resources, R.drawable.rook_black),
        Type.BISHOP to BitmapFactory.decodeResource(resources, R.drawable.bishop_black),
        Type.KNIGHT to BitmapFactory.decodeResource(resources, R.drawable.knight_black),
        Type.PAWN to BitmapFactory.decodeResource(resources, R.drawable.pawn_black)
    )
}

private fun getPieceBitmap(piece: Piece): Bitmap? {
    return BITMAPS[piece.player]?.get(piece.type)
}

// getPaint returns a paint with the given color
private fun getPaint(color: Int): Paint {
    val paint = Paint()
    paint.color = color

    return paint
}