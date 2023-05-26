package com.idodanieli.playit.games.chess.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.MODE_ONLINE
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.*
import kotlin.math.min

const val CHESSBOARD_SIZE = 8

var BITMAPS: MutableMap<Player, MutableMap<String, Bitmap>> = mutableMapOf()

class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var mode = MODE_ONLINE // TODO: Change this in the future
    private val chessDrawer = ChessDrawer(CHESSBOARD_SIZE, mode, context!!)
    private val moveSound = MediaPlayer.create(context, R.raw.sound_chess_move)
    private val gameOverSound = MediaPlayer.create(context, R.raw.sound_game_over)
    private var gameListener: GameListener? = null

    private var squareSize = 0f
    private var movingPiece: MovingPiece? = null
    private var previousTouchedSquare: Square? = null
    private var currentlyTouchedSquare: Square? = null
    private var availableSquares: List<Square> = listOf()
    private var touchedPiece: Piece? = null
    var game: Game = Game("Default", mutableSetOf(), 0)

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

        chessDrawer.canvas = canvas
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
            val move = Move(previousTouchedSquare, touchedSquare)

            if (playerMadeAMove(touchedSquare) && game.canMove(move)) {
                movePiece(move)
                switchTurn()
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
            val move = Move(previousTouchedSquare, touchedSquare)
            if (playerMadeAMove(touchedSquare) && !game.canMove(move)) {
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

    private fun movePiece(move: Move) {
        game.movePiece(move.origin, move.dest)

        gameListener?.onPieceMoved(move)

        moveSound.start()
    }

    private fun switchTurn() {
        game.switchTurn()

        if (game.isOver()) {
            onGameOver()
        }

        gameListener?.onTurnSwitched(this)
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
    private fun playerMadeAMove(touchedSquare: Square): Boolean {
        return previousTouchedSquare != null
                && touchedSquare.inBorder(game.size)
                && previousTouchedSquare != touchedSquare
    }


    fun setGameListener(listener: GameListener) {
        gameListener = listener
    }
}

data class MovingPiece(val piece: Piece, var x: Float, var y: Float, val bitmap: Bitmap, var player: Player)