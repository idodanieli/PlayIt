package com.idodanieli.playit.games.chess.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.*
import com.idodanieli.playit.games.chess.pieces.classic.*
import com.idodanieli.playit.games.chess.pieces.fairy.*
import kotlin.math.min

const val CHESSBOARD_SIZE = 8

var BITMAPS: MutableMap<Player, MutableMap<String, Bitmap>> = mutableMapOf()

class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val chessDrawer = ChessDrawer(CHESSBOARD_SIZE, COLOR_LIGHT, COLOR_DARK)
    private val moveSound = MediaPlayer.create(context, R.raw.sound_chess_move)
    private val gameOverSound = MediaPlayer.create(context, R.raw.sound_game_over)
    private var gameListener: GameListener? = null

    private var squareSize = 0f
    private var movingPiece: MovingPiece? = null
    private var previousTouchedSquare: Square? = null
    private var currentlyTouchedSquare: Square? = null
    private var availableSquares: List<Square> = listOf()
    private var touchedPiece: Piece? = null
    var game: Game = Game("Default", mutableSetOf(), 0, "")

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
            // This action occurs when the user initially presses down on the screen
            MotionEvent.ACTION_DOWN -> {
                onTouchDown(touchedSquare)
            }

            // This action occurs when the user moves their finger on the screen after pressing down.
            MotionEvent.ACTION_MOVE -> {
                // onTouchMove(event, touchedSquare)
            }

            //  This action occurs when the user releases their finger or lifts the stylus from the screen
            MotionEvent.ACTION_UP -> {
                onTouchUp(touchedSquare)
                invalidate()
            }
        }

        return true
    }

    //////////////////////// OnTouch Functions \\\\\\\\\\\\\\\\\\\\\\\\
    private fun onGameOver() {
        gameOverSound.start()
        gameListener?.onGameOver(game.currentPlayer.opposite())
    }

    private fun onTouchUp(touchedSquare: Square) {
        currentlyTouchedSquare = touchedSquare
        previousTouchedSquare?.let { previousTouchedSquare ->
            if (currentlyTouchedSquare != previousTouchedSquare) {
                if (playerTriesToMove(touchedSquare) && game.canMove(previousTouchedSquare, touchedSquare)) {
                    movePiece(previousTouchedSquare, touchedSquare)

                    if (game.isOver()) {
                        onGameOver()
                    }
                }
                resetVisuals()
            }
            return
        }

        // Previous Square is null
        previousTouchedSquare = touchedSquare

        touchedPiece = game.board.pieceAt(touchedSquare)
        touchedPiece?.let {
            if (game.currentPlayer != it.player) {
                resetVisuals()
                return
            }

            availableSquares = game.validMoves(it)
        }
    }

    private fun onTouchDown(touchedSquare: Square) {
        previousTouchedSquare?.let { previousTouchedSquare ->
            if (playerTriesToMove(touchedSquare) && !game.canMove(previousTouchedSquare, touchedSquare)) {
                resetVisuals()
            }
        }
    }

    private fun onTouchMove(event: MotionEvent, touchedSquare: Square) {
        currentlyTouchedSquare = touchedSquare
        movingPiece?.let {
            it.x = event.x
            it.y = event.y
        }

        invalidate() // calls onDraw
    }

    //////////////////////// OnTouch Functions \\\\\\\\\\\\\\\\\\\\\\\\

    private fun movePiece(previousSquare: Square, touchedSquare: Square) {
        game.movePiece(previousSquare, touchedSquare)
        game.currentPlayer = game.currentPlayer.opposite()

        moveSound.start()
    }

    private fun resetVisuals() {
        touchedPiece = null
        movingPiece = null
        currentlyTouchedSquare = null
        previousTouchedSquare = null
    }

    // getSquareTouched returns the square touched by the position in the MotionEvent
    private fun getTouchedSquare(event: MotionEvent): Square {
        val touchedColumn = (event.x / squareSize).toInt()
        val touchedRow = 7 - (event.y / squareSize).toInt()

        return Square(touchedColumn, touchedRow)
    }

    // playerTriesToMove returns true if the player made a move inside of the board
    private fun playerTriesToMove(touchedSquare: Square): Boolean {
        return previousTouchedSquare != null
                && touchedSquare.inBorder(game.size)
                && previousTouchedSquare != touchedSquare
    }


    fun setGameListener(listener: GameListener) {
        gameListener = listener
    }
}

data class MovingPiece(val piece: Piece, var x: Float, var y: Float, val bitmap: Bitmap, var player: Player)

//// HELPER FUNCTIONS \\\\

fun getPieceBitmap(piece: Piece): Bitmap? {
    return BITMAPS[piece.player]?.get(piece.type)
}

private fun loadBitmaps(resources: Resources) {
    if (BITMAPS.isNotEmpty()) { return }

    BITMAPS[Player.WHITE] = mutableMapOf(
        TYPE_KING to BitmapFactory.decodeResource(resources, R.drawable.king_white),
        TYPE_QUEEN to BitmapFactory.decodeResource(resources, R.drawable.queen_white),
        TYPE_ROOK to BitmapFactory.decodeResource(resources, R.drawable.rook_white),
        TYPE_BISHOP to BitmapFactory.decodeResource(resources, R.drawable.bishop_white),
        TYPE_KNIGHT to BitmapFactory.decodeResource(resources, R.drawable.knight_white),
        TYPE_PAWN to BitmapFactory.decodeResource(resources, R.drawable.pawn_white),
        TYPE_VENOM to BitmapFactory.decodeResource(resources, R.drawable.venom_white),
        TYPE_BEROLINA_PAWN to BitmapFactory.decodeResource(resources, R.drawable.berolina_white),
        TYPE_GIRAFFE to BitmapFactory.decodeResource(resources, R.drawable.giraffe_white),
        TYPE_ZEBRA to BitmapFactory.decodeResource(resources, R.drawable.zebra_white),
        TYPE_CENTAUR to BitmapFactory.decodeResource(resources, R.drawable.centaur_white),
        TYPE_ELEPHANT to BitmapFactory.decodeResource(resources, R.drawable.elephant_white),
        TYPE_GRASSHOPPER to BitmapFactory.decodeResource(resources, R.drawable.grasshopper_white),
        TYPE_CAMEL to BitmapFactory.decodeResource(resources, R.drawable.camel_white),
        TYPE_WILDBEAST to BitmapFactory.decodeResource(resources, R.drawable.wildbeast_white),
        TYPE_AMAZON to BitmapFactory.decodeResource(resources, R.drawable.amazon_white),
        TYPE_EMPRESS to BitmapFactory.decodeResource(resources, R.drawable.empress_white),
        TYPE_ARCHBISHOP to BitmapFactory.decodeResource(resources, R.drawable.archbishop_white),
        TYPE_XIANGQI_HORSE to BitmapFactory.decodeResource(resources, R.drawable.xiangqi_horse_white),
    )

    BITMAPS[Player.BLACK] = mutableMapOf(
        TYPE_KING to BitmapFactory.decodeResource(resources, R.drawable.king_black),
        TYPE_QUEEN to BitmapFactory.decodeResource(resources, R.drawable.queen_black),
        TYPE_ROOK to BitmapFactory.decodeResource(resources, R.drawable.rook_black),
        TYPE_BISHOP to BitmapFactory.decodeResource(resources, R.drawable.bishop_black),
        TYPE_KNIGHT to BitmapFactory.decodeResource(resources, R.drawable.knight_black),
        TYPE_PAWN to BitmapFactory.decodeResource(resources, R.drawable.pawn_black),
        TYPE_VENOM to BitmapFactory.decodeResource(resources, R.drawable.venom_black),
        TYPE_BEROLINA_PAWN to BitmapFactory.decodeResource(resources, R.drawable.berolina_black),
        TYPE_GIRAFFE to BitmapFactory.decodeResource(resources, R.drawable.giraffe_black),
        TYPE_ZEBRA to BitmapFactory.decodeResource(resources, R.drawable.zebra_black),
        TYPE_CENTAUR to BitmapFactory.decodeResource(resources, R.drawable.centaur_black),
        TYPE_ELEPHANT to BitmapFactory.decodeResource(resources, R.drawable.elephant_black),
        TYPE_GRASSHOPPER to BitmapFactory.decodeResource(resources, R.drawable.grasshopper_black),
        TYPE_CAMEL to BitmapFactory.decodeResource(resources, R.drawable.camel_black),
        TYPE_WILDBEAST to BitmapFactory.decodeResource(resources, R.drawable.wildbeast_black),
        TYPE_AMAZON to BitmapFactory.decodeResource(resources, R.drawable.amazon_black),
        TYPE_EMPRESS to BitmapFactory.decodeResource(resources, R.drawable.empress_black),
        TYPE_ARCHBISHOP to BitmapFactory.decodeResource(resources, R.drawable.archbishop_black),
        TYPE_XIANGQI_HORSE to BitmapFactory.decodeResource(resources, R.drawable.xiangqi_horse_black),
    )
}

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()