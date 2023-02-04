package com.idodanieli.playit.games.chess

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.Player
import com.idodanieli.playit.games.chess.pieces.Type
import com.idodanieli.playit.games.chess.pieces.Piece
import kotlin.math.min

private const val CHESSBOARD_SIZE = 8
private const val MOVING_PIECE_SCALE = 1.5f
private const val MOVING_PIECE_Y_OFFSET = 150f // so the user will see what piece hes moving

private var BITMAPS: MutableMap<Player, MutableMap<Type, Bitmap>> = mutableMapOf()
private val COLOR_LIGHT = Color.parseColor("#ffe9c5")
private val COLOR_DARK = Color.parseColor("#855E42")
private val COLOR_TOUCHED = Color.parseColor("#CBC3E3")
private val COLOR_LIGHT_AVAILABLE_SQUARE = Color.parseColor("#FF7276")
private val COLOR_DARK_AVAILABLE_SQUARE = Color.parseColor("#E6676B")


class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val chessDrawer = ChessDrawer(CHESSBOARD_SIZE, COLOR_LIGHT, COLOR_DARK)
    private var squareSize = 0f
    private var movingPiece: MovingPiece? = null
    private var previousTouchedSquare: Square = Square(-1, -1)
    private var currentlyTouchedSquare: Square? = null
    private var availableSquares: List<Square> = listOf()
    private var touchedPiece: Piece? = null
    private var game: Game = Game(classicPiecesSet(), CHESSBOARD_SIZE)

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

        squareSize = width / this.game.size.toFloat()

        chessDrawer.setCanvas(canvas)
        chessDrawer.setSize(squareSize)

        chessDrawer.drawChessboard()

        this.drawTouchEvents()

        chessDrawer.drawPieces(game, movingPiece)
    }

    private fun drawTouchEvents() {
        // if a square / piece is touched, it will be highlighted purple for indication
        currentlyTouchedSquare?.let {
            chessDrawer.drawSquare(it, COLOR_TOUCHED)
        }

        touchedPiece?.let {
            chessDrawer.drawSquare(it.square, COLOR_TOUCHED)
            chessDrawer.drawSquares(availableSquares, COLOR_LIGHT_AVAILABLE_SQUARE, COLOR_DARK_AVAILABLE_SQUARE)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        val touchedSquare = getTouchedSquare(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                previousTouchedSquare = touchedSquare
                currentlyTouchedSquare = touchedSquare
                touchedPiece = game.board.pieceAt(touchedSquare)

                touchedPiece?.let {
                    if (game.currentPlayer != it.player) {
                        return false
                    }

                    movingPiece = MovingPiece(it, event.x, event.y, getPieceBitmap(it)!!, it.player)
                    availableSquares = it.availableSquares(game.board)
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
                if (previousTouchedSquare != touchedSquare &&
                    game.board.canMove(previousTouchedSquare, touchedSquare)) {
                    game.movePiece(previousTouchedSquare, touchedSquare)
                    touchedPiece = null
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

private class ChessDrawer(private val size: Int, private val lightColor: Int, private val darkColor: Int) {
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
            this.drawBitmapAtPosition(it.x, it.y - MOVING_PIECE_Y_OFFSET, it.bitmap, scale = MOVING_PIECE_SCALE)
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

private data class MovingPiece(val piece: Piece, var x: Float, var y: Float, val bitmap: Bitmap, var player: Player)

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