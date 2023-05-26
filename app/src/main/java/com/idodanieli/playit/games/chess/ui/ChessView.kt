package com.idodanieli.playit.games.chess.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.CHESS_GAME_LISTENER
import com.idodanieli.playit.games.chess.MODE_DEFAULT
import com.idodanieli.playit.games.chess.game_listener.ChessGameListener
import com.idodanieli.playit.games.chess.game_listener.GameListener
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.*
import kotlin.math.min

const val CHESSBOARD_SIZE = 8

var BITMAPS: MutableMap<Player, MutableMap<String, Bitmap>> = mutableMapOf()

class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val chessDrawer = ChessDrawer(CHESSBOARD_SIZE, MODE_DEFAULT, context!!)
    private val moveSound = MediaPlayer.create(context, R.raw.sound_chess_move)
    private val gameOverSound = MediaPlayer.create(context, R.raw.sound_game_over)
    private var chessGameListener: ChessGameListener? = null
    private var gameListener: GameListener? = null

    private var squareSize = 0f
    private var movingPiece: MovingPiece? = null
    private var previousTouchedSquare: Square? = null
    private var currentlyTouchedSquare: Square? = null
    private var availableSquares: List<Square> = listOf()
    private var touchedPiece: Piece? = null

    var hero = Player.WHITE
    var game: Game = Game("Default", mutableSetOf(), 0)
    var gameStarted: Boolean = false

    fun startGame() {
        chessGameListener?.onGameStarted(this)
        gameStarted = true
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
        if (!gameStarted || !canHeroPlay()) { return true }

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
            val move = Move(previousTouchedSquare, touchedSquare, hero)

            if (heroMadeMove(touchedSquare) && game.canMove(move)) {
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
            val move = Move(previousTouchedSquare, touchedSquare, hero)
            if (heroMadeMove(touchedSquare) && !game.canMove(move)) {
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

        chessGameListener?.onPieceMoved(move)

        moveSound.start()
    }

    private fun switchTurn() {
        game.switchTurn()

        if (game.isOver()) {
            onGameOver()
        }

        chessGameListener?.onTurnSwitched(this)
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

    // cnaHeroMove returns true if the hero can play
    private fun canHeroPlay(): Boolean {
        chessGameListener?.let {
            if (!it.canHeroPlay(this)) { return false }
        }

        return true
    }

    // playerTriesToMove returns true if the player made a move inside of the board
    private fun heroMadeMove(touchedSquare: Square): Boolean {
        return previousTouchedSquare != null
                && touchedSquare.inBorder(game.size)
                && previousTouchedSquare != touchedSquare
    }

    fun setGameListener(listener: GameListener) {
        gameListener = listener
    }
    fun setMode(mode: String) {
        chessDrawer.mode = mode
        chessGameListener = CHESS_GAME_LISTENER[mode]
    }
    fun setGameHero(hero: Player) {
        this.hero = hero
        chessDrawer.setHero(hero)
    }
}

data class MovingPiece(val piece: Piece, var x: Float, var y: Float, val bitmap: Bitmap, var player: Player)