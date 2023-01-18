package com.idodanieli.playit.games.chess

import android.annotation.SuppressLint
import android.content.Context
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

    private var movingPieceBitmap: Bitmap? = null
    private var movingPiece: Piece? = null
    private var previousTouchedSquare: Square = Square(-1, -1)
    private var currentlyTouchedSquare: Square? = null
    private var movingPieceX = -1f
    private var movingPieceY = -1f

    var board: Board? = Board()

    init {
        loadBitmaps()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(smaller, smaller)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        squareSize = width / CHESS_BOARD_SIZE.toFloat()

        chessDrawer.setCanvas(canvas)
        chessDrawer.setSize(squareSize)

        chessDrawer.drawChessboard()

        currentlyTouchedSquare?.let {
            chessDrawer.drawSquare(it, COLOR_TOUCHED)
        }

        drawPieces()
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
                    movingPiece = it
                    movingPieceBitmap = getPieceBitmap(it)
                    Log.d("CHESS", "clicked ${movingPiece!!.type.name}")
                }
            }
            MotionEvent.ACTION_MOVE -> {
                currentlyTouchedSquare = touchedSquare
                movingPieceX = event.x
                movingPieceY = event.y
                invalidate() // calls onDraw
            }
            MotionEvent.ACTION_UP -> {
                if (previousTouchedSquare.col != touchedSquare.col || previousTouchedSquare.row != touchedSquare.row) {
                    board?.movePiece(previousTouchedSquare, touchedSquare)
                }
                movingPiece = null
                movingPieceBitmap = null
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

    private fun drawPieces() {
        for (row in 0 until CHESS_BOARD_SIZE) {
            for (col in 0 until CHESS_BOARD_SIZE) {
                val square = Square(col, row)
                board?.pieceAt(square)?.let { piece ->
                    if (piece != movingPiece) {
                        chessDrawer.drawPieceAtSquare(square, getPieceBitmap(piece)!!)
                    }
                }
            }
        }

        movingPieceBitmap?.let {
            chessDrawer.drawPieceAtPosition(movingPieceX, movingPieceY - MOVING_PIECE_Y_OFFSET, it, scale = MOVING_PIECE_SCALE)
        }
    }

    private fun getPieceBitmap(piece: Piece): Bitmap? {
        return BITMAPS[piece.player]?.get(piece.type)
    }

    private fun loadBitmaps() {
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

    // drawSquare draws a square
    fun drawPieceAtSquare(square: Square, bitmap: Bitmap) =
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

    // drawPieceAtPosition draws the piece in the given position on the screen
    // @param scale = the scaling of the piece
    fun drawPieceAtPosition(x: Float, y: Float, bitmap: Bitmap, scale: Float = 1f) =
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

    fun drawChessboard() {
        for (row in 0 until CHESS_BOARD_SIZE) {
            for (col in 0 until CHESS_BOARD_SIZE) {
                val square = Square(col, row)
                val isDark = (square.col + square.row) % 2 == 1
                this.drawSquare(square, if (isDark) darkColor else lightColor)
            }
        }
    }

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