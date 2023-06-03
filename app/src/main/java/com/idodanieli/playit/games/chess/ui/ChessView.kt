package com.idodanieli.playit.games.chess.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.CHESS_GAME_LISTENER
import com.idodanieli.playit.games.chess.MODE_DEFAULT
import com.idodanieli.playit.games.chess.game_listener.ChessGameListener
import com.idodanieli.playit.games.chess.game_listener.GameListener
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.*
import kotlin.math.min

class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    // --- For Drawing ------------------------------------------------------------------------- \\
    private val chessDrawer = ChessDrawer(CHESSBOARD_SIZE, MODE_DEFAULT, context!!)
    private var touchedPiece: Piece? = null
    private var movingPiece: MovingPiece? = null
    private var squareSize = 0f

    // --- For Sounds ------------------------------------------------------------------------- \\
    private val soundMove = MediaPlayer.create(context, R.raw.sound_chess_move)
    private val soundGameOver = MediaPlayer.create(context, R.raw.sound_game_over)

    // --- For Logic ------------------------------------------------------------------------- \\
    private var chessGameListener: ChessGameListener? = null
    private var gameListener: GameListener? = null
    private var previousTouchedSquare: Square? = null

    var hero = Player.WHITE
    var game: Game = Game("Default", mutableSetOf(), 0)

    // TODO: Delete this later
    var currentPlayer: TextView? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(smaller, smaller)
    }

    // --- onDraw ------------------------------------------------------------------------------ \\
    // onDraw is called everytime invalidate() is called
    // the order of the draw functions inside is crucial
    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        squareSize = width / this.game.size.toFloat()

        chessDrawer.canvas = canvas
        chessDrawer.setSize(squareSize)

        chessDrawer.drawChessboard()

        drawTouchedPiece()

        chessDrawer.drawPieces(game, movingPiece)

        drawCurrentPlayer()
    }

    private fun drawCurrentPlayer() {
        currentPlayer?.let {
            if (game.currentPlayer.isBlack()) {
                it.setTextColor(resources.getColor(R.color.black))
            } else {
                it.setTextColor(resources.getColor(R.color.white))
            }
        }
    }

    private fun drawTouchedPiece() {
        touchedPiece ?: return

        // If the touched piece is not of the current player - display nothing
        if (game.currentPlayer != touchedPiece!!.player) { return }

        chessDrawer.drawSquare(touchedPiece!!.square, COLOR_TOUCHED)
        chessDrawer.drawAvailableSquares(getAvailableSquares(touchedPiece!!))
    }

    private fun resetVisuals() {
        touchedPiece = null
        movingPiece = null
        previousTouchedSquare = null
    }

    // --- OnTouch ----------------------------------------------------------------------------- \\
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        if (!game.started || !canHeroPlay()) { return true }

        var touchedSquare = getTouchedSquare(event)

        // When the player is black the screen is flipped vertically
        if (hero.isBlack()) {
            touchedSquare = touchedSquare.flipVertically(game.size)
        }

        when (event.action) {
            // This action occurs when the user initially presses down on the screen
            MotionEvent.ACTION_DOWN -> {
                onPressed(touchedSquare)
            }

            // This action occurs when the user moves their finger on the screen after pressing down.
            MotionEvent.ACTION_MOVE -> {
                // onTouchMove(event, touchedSquare)
            }

            //  This action occurs when the user releases their finger or lifts the stylus from the screen
            MotionEvent.ACTION_UP -> {
                onReleased(touchedSquare)
                invalidate()
            }
        }

        return true
    }

    private fun onGameOver() {
        soundGameOver.start()
        gameListener?.onGameOver(game.currentPlayer.opposite())
        chessGameListener?.onGameOver()
    }

    private fun onPressed(touchedSquare: Square) {
        previousTouchedSquare?.let { previousTouchedSquare ->
            val move = Move(previousTouchedSquare, touchedSquare, hero)
            if (heroMadeMove(touchedSquare) && !game.isLegalMove(move)) {
                resetVisuals()
            }
        }
    }

    private fun onDragged(event: MotionEvent, touchedSquare: Square) {
        movingPiece?.let {
            it.x = event.x
            it.y = event.y
        }

        invalidate() // calls onDraw
    }

    private fun onReleased(touchedSquare: Square) {
        previousTouchedSquare?.let { previousTouchedSquare ->
            val move = Move(previousTouchedSquare, touchedSquare, hero)

            if (heroMadeMove(touchedSquare) && game.isLegalMove(move)) {
                movePiece(move)
                chessGameListener?.onPieceMoved(move)
            }
            return
        }

        // Previous Square is null
        previousTouchedSquare = touchedSquare

        touchedPiece = game.board.pieceAt(touchedSquare)
    }

    // getSquareTouched returns the square touched by the position in the MotionEvent
    private fun getTouchedSquare(event: MotionEvent): Square {
        val touchedColumn = (event.x / squareSize).toInt()
        val touchedRow = 7 - (event.y / squareSize).toInt()

        return Square(touchedColumn, touchedRow)
    }

    // --- View Game Logic --------------------------------------------------------------------- \\

    fun startGame(gameID: String = "") {
        chessGameListener?.onGameStarted(this, gameID)
        game.started = true
    }

    // movePiece in the game, will be shown in the UI
    fun movePiece(move: Move) {
        game.movePiece(move.origin, move.dest)
        soundMove.start()

        game.switchTurn()
        if (game.isOver()) {
            onGameOver()
        }

        resetVisuals()
        invalidate()
    }

    private fun getAvailableSquares(piece: Piece): List<Square> {
        val squares = game.getLegalMovesForPiece(piece)

        // When the player is black the screen is flipped vertically
        if (hero.isBlack()) {
            return squares.map { it.flipVertically(game.size) }
        }

        return squares
    }

    // playerTriesToMove returns true if the player made a move inside of the board
    private fun heroMadeMove(touchedSquare: Square): Boolean {
        return previousTouchedSquare != null && previousTouchedSquare != touchedSquare
    }

    // cnaHeroMove returns true if the hero can play
    private fun canHeroPlay(): Boolean {
        chessGameListener?.let {
            if (!it.canHeroPlay(this)) { return false }
        }

        return true
    }

    // isOpponentsTurn returns true if its the opponents turn and not the heros
    fun isOpponentsTurn(): Boolean {
        return game.currentPlayer != hero
    }

    // --- General ----------------------------------------------------------------------------- \\
    fun setMode(mode: String) {
        chessDrawer.mode = mode
        chessGameListener = CHESS_GAME_LISTENER[mode]
    }
    fun setGameHero(hero: Player) {
        this.hero = hero
        this.chessDrawer.hero = hero
    }
    fun setGameListener(gameListener: GameListener) {
        this.gameListener = gameListener
    }
}

data class MovingPiece(val piece: Piece, var x: Float, var y: Float, val bitmap: Bitmap, var player: Player)