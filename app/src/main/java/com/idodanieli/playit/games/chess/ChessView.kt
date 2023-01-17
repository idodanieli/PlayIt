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

private const val LIGHT_TILE_COLOR = "#ffe9c5"
private const val DARK_TILE_COLOR = "#855E42"
private const val CHESS_BOARD_SIZE = 8
private const val MOVING_PIECE_SCALE = 1f // 0.5 to keep original size
private const val MOVING_PIECE_Y_OFFSET = 150f // so the user will see what piece hes moving
private var BITMAPS: MutableMap<Player, MutableMap<Type, Bitmap>> = mutableMapOf()


class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var cellSize = 0f
    private val lightColor = Color.parseColor(LIGHT_TILE_COLOR)
    private val darkColor = Color.parseColor(DARK_TILE_COLOR)
    private val paint = Paint()

    private var movingPieceBitmap: Bitmap? = null
    private var movingPiece: Piece? = null
    private var previousTouchedSquare: Square = Square(-1, -1)
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

        val chessBoardSide = min(width, height)

        cellSize = chessBoardSide / CHESS_BOARD_SIZE.toFloat()

        drawChessboard(canvas)
        drawPieces(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        val touchedSquare = getTouchedSquare(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                previousTouchedSquare = touchedSquare
                board?.pieceAt(touchedSquare)?.let {
                    movingPiece = it
                    movingPieceBitmap = getPieceBitmap(it)
                    Log.d("CHESS", "clicked ${movingPiece!!.type.name}")
                }
            }
            MotionEvent.ACTION_MOVE -> {
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
                invalidate() // calls onDraw
            }
        }
        return true
    }

    // getSquareTouched returns the square touched by the position in the MotionEvent
    private fun getTouchedSquare(event: MotionEvent): Square {
        val touchedColumn = (event.x / cellSize).toInt()
        val touchedRow = 7 - (event.y / cellSize).toInt()

        return Square(touchedColumn, touchedRow)
    }

    private fun drawPieces(canvas: Canvas) {
        for (row in 0 until CHESS_BOARD_SIZE) {
            for (col in 0 until CHESS_BOARD_SIZE) {
                val square = Square(col, row)
                board?.pieceAt(square)?.let { piece ->
                    if (piece != movingPiece) {
                        drawPieceAtSquare(canvas, square, getPieceBitmap(piece)!!)
                    }
                }
            }
        }

        drawMovingPiece(canvas)
    }

    // drawMovingPiece draws the piece that is currently touched by the user
    private fun drawMovingPiece(canvas: Canvas)  =
        movingPieceBitmap?.let { bitmap ->
            canvas.drawBitmap(
                bitmap,
                null,
                RectF(
                    movingPieceX - cellSize * MOVING_PIECE_SCALE,
                    movingPieceY - cellSize * MOVING_PIECE_SCALE - MOVING_PIECE_Y_OFFSET,
                    movingPieceX + cellSize * MOVING_PIECE_SCALE,
                    movingPieceY + cellSize * MOVING_PIECE_SCALE - MOVING_PIECE_Y_OFFSET
                ),
                paint
            )
        }

    private fun drawPieceAtSquare(canvas: Canvas, square: Square, bitmap: Bitmap) =
        canvas.drawBitmap(
            bitmap,
            null,
            RectF(
                square.col * cellSize,
                (7 - square.row) * cellSize,
                (square.col + 1) * cellSize,
                ((7 - square.row) + 1) * cellSize
            ),
            paint
        )

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

    private fun drawChessboard(canvas: Canvas) {
        for (row in 0 until CHESS_BOARD_SIZE)
            for (col in 0 until CHESS_BOARD_SIZE)
                drawSquare(canvas, Square(col, row))
    }

    // TODO: Extend canvas and move this function to it
    private fun drawSquare(canvas: Canvas, square: Square) {
        val isDark =  (square.col + square.row) % 2 == 1
        paint.color = if (isDark) darkColor else lightColor
        canvas.drawRect(
             square.col * cellSize,
            square.row * cellSize,
            (square.col + 1) * cellSize,
            (square.row + 1) * cellSize,
            paint
        )
    }

    private fun getPieceBitmap(piece: Piece): Bitmap? {
        return BITMAPS[piece.player]?.get(piece.type)
    }
}