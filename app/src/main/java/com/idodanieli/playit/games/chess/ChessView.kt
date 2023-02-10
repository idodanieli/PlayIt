package com.idodanieli.playit.games.chess

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.pieces.*
import kotlin.math.min

const val CHESSBOARD_SIZE = 8

var BITMAPS: MutableMap<Player, MutableMap<Type, Bitmap>> = mutableMapOf()

class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val chessDrawer = ChessDrawer(CHESSBOARD_SIZE, COLOR_LIGHT, COLOR_DARK)
    private val moveSound = MediaPlayer.create(context, R.raw.sound_chess_move)
    private val gameOverSound = MediaPlayer.create(context, R.raw.sound_game_over)

    private var squareSize = 0f
    private var movingPiece: MovingPiece? = null
    private var previousTouchedSquare: Square = Square(-1, -1)
    private var currentlyTouchedSquare: Square? = null
    private var availableSquares: List<Square> = listOf()
    private var touchedPiece: Piece? = null
    var game: Game = Game("Default", mutableSetOf(), 0)

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
        if (game.isOver()) { return false }

        val touchedSquare = getTouchedSquare(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (previousTouchedSquare.isValid(game.size) && previousTouchedSquare != touchedSquare) {
                    if (tryToMovePiece(touchedSquare)) {
                        invalidate()
                        return true
                    }
                }

                previousTouchedSquare = touchedSquare
                currentlyTouchedSquare = touchedSquare
                touchedPiece = game.board.pieceAt(touchedSquare)

                touchedPiece?.let {
                    if (game.currentPlayer != it.player) {
                        return false
                    }

                    movingPiece = MovingPiece(it, event.x, event.y, getPieceBitmap(it)!!, it.player)
                    availableSquares = it.validMoves(game.board)
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
                tryToMovePiece(touchedSquare)
                movingPiece = null
                currentlyTouchedSquare = null
                invalidate() // calls onDraw
            }
        }
        return true
    }

    private fun tryToMovePiece(touchedSquare: Square): Boolean {
        if (previousTouchedSquare != touchedSquare &&
            game.canMove(previousTouchedSquare, touchedSquare)) {
            game.movePiece(previousTouchedSquare, touchedSquare)
            touchedPiece = null

            if (game.isOver()) {
                context.toast("${game.currentPlayer} Won!")
                gameOverSound.start()
            } else {
                moveSound.start()
                game.currentPlayer = game.currentPlayer.opposite()
            }
            return true
        }

        previousTouchedSquare = touchedSquare
        return false
    }

    // getSquareTouched returns the square touched by the position in the MotionEvent
    private fun getTouchedSquare(event: MotionEvent): Square {
        val touchedColumn = (event.x / squareSize).toInt()
        val touchedRow = 7 - (event.y / squareSize).toInt()

        return Square(touchedColumn, touchedRow)
    }
}

data class MovingPiece(val piece: Piece, var x: Float, var y: Float, val bitmap: Bitmap, var player: Player)

//// HELPER FUNCTIONS \\\\

fun getPieceBitmap(piece: Piece): Bitmap? {
    return BITMAPS[piece.player]?.get(piece.type)
}

private fun loadBitmaps(resources: Resources) {
    BITMAPS[Player.WHITE] = mutableMapOf(
        Type.KING to BitmapFactory.decodeResource(resources, R.drawable.king_white),
        Type.QUEEN to BitmapFactory.decodeResource(resources, R.drawable.queen_white),
        Type.ROOK to BitmapFactory.decodeResource(resources, R.drawable.rook_white),
        Type.BISHOP to BitmapFactory.decodeResource(resources, R.drawable.bishop_white),
        Type.KNIGHT to BitmapFactory.decodeResource(resources, R.drawable.knight_white),
        Type.PAWN to BitmapFactory.decodeResource(resources, R.drawable.pawn_white),
        Type.VENOM to BitmapFactory.decodeResource(resources, R.drawable.venom_white),
        Type.BEROLINA to BitmapFactory.decodeResource(resources, R.drawable.berolina_white),
        Type.GIRAFFE to BitmapFactory.decodeResource(resources, R.drawable.giraffe_white),
        Type.ZEBRA to BitmapFactory.decodeResource(resources, R.drawable.zebra_white),
        Type.CENTAUR to BitmapFactory.decodeResource(resources, R.drawable.centaur_white),
    )

    BITMAPS[Player.BLACK] = mutableMapOf(
        Type.KING to BitmapFactory.decodeResource(resources, R.drawable.king_black),
        Type.QUEEN to BitmapFactory.decodeResource(resources, R.drawable.queen_black),
        Type.ROOK to BitmapFactory.decodeResource(resources, R.drawable.rook_black),
        Type.BISHOP to BitmapFactory.decodeResource(resources, R.drawable.bishop_black),
        Type.KNIGHT to BitmapFactory.decodeResource(resources, R.drawable.knight_black),
        Type.PAWN to BitmapFactory.decodeResource(resources, R.drawable.pawn_black),
        Type.VENOM to BitmapFactory.decodeResource(resources, R.drawable.venom_black),
        Type.BEROLINA to BitmapFactory.decodeResource(resources, R.drawable.berolina_black),
        Type.GIRAFFE to BitmapFactory.decodeResource(resources, R.drawable.giraffe_black),
        Type.ZEBRA to BitmapFactory.decodeResource(resources, R.drawable.zebra_black),
        Type.CENTAUR to BitmapFactory.decodeResource(resources, R.drawable.centaur_black),
    )
}

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()