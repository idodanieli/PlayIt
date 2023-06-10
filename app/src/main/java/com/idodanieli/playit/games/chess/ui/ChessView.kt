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
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.*
import kotlin.math.min

class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs), GameSubscriber {
    // --- For Drawing -----------------------------------------------------------------------------
    private val chessDrawer = ChessDrawer(CHESSBOARD_SIZE, MODE_DEFAULT, context!!)
    private var touchedPiece: Piece? = null
    private var movingPiece: MovingPiece? = null
    private var squareSize = 0f

    // --- For Sounds ------------------------------------------------------------------------------
    private val soundMove = MediaPlayer.create(context, R.raw.sound_chess_move)
    private val soundGameOver = MediaPlayer.create(context, R.raw.sound_game_over)

    // --- For Logic -------------------------------------------------------------------------------
    private var chessGameListener: ChessGameListener? = null
    private var touchedPieceAvailableMoves = emptyMap<Move, Move>()
    private val publisher = Publisher()

    var hero = Player.WHITE
    var game: Game = Game("Default", mutableSetOf(), 0)

    // --- Views -----------------------------------------------------------------------------------
    lateinit var heroTextView: TextView
    lateinit var opponentsCapturedPieces: CapturedPiecesView

    lateinit var opponentTextView: TextView
    lateinit var heroCapturedPieces: CapturedPiecesView

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(smaller, smaller)
    }

    // --- Subscriber ------------------------------------------------------------------------------
    override fun onGameEvent(event: GameEvent) {
        when(event) {
            is PieceCapturedEvent -> {
                onCapturedPiece(event.capturedPiece)
            }
        }
    }

    private fun onCapturedPiece(capturedPiece: Piece) {
        if (hero != capturedPiece.player) {
            opponentsCapturedPieces.append(capturedPiece)
            return
        }

        heroCapturedPieces.append(capturedPiece)
    }

    // --- Publisher -------------------------------------------------------------------------------
    fun subscribe(subscriber: GameSubscriber) {
        publisher.subscribe(subscriber)
        game.subscribe(subscriber)
    }

    // --- onDraw ----------------------------------------------------------------------------------
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
    }

    private fun drawTouchedPiece() {
        touchedPiece ?: return

        chessDrawer.drawAvailableMoves(touchedPieceAvailableMoves.keys)
        chessDrawer.drawTouchedSquare(touchedPiece!!.square)
    }

    private fun resetVisuals() {
        touchedPiece = null
        movingPiece = null
        touchedPieceAvailableMoves = emptyMap()

        invalidate()
    }

    // --- OnTouch ----------------------------------------------------------------------------- \\
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        if (!game.started || !canHeroPlay()) { return true }

        val touchedSquare = getTouchedSquare(event)

        when (event.action) {
            // This action occurs when the user initially presses down on the screen
            MotionEvent.ACTION_DOWN -> {
                onTouchPressed(touchedSquare)
            }

            // This action occurs when the user moves their finger on the screen after pressing down.
            MotionEvent.ACTION_MOVE -> {
                // onTouchMove(event, touchedSquare)
            }

            //  This action occurs when the user releases their finger or lifts the stylus from the screen
            MotionEvent.ACTION_UP -> {
                onTouchReleased(touchedSquare)
                invalidate()
            }
        }

        return true
    }

    private fun onGameOver() {
        val winner = game.currentPlayer.opposite()
        val gameOverEvent = GameOverEvent(winner)

        game.notifySubscribers(gameOverEvent)
        game.unsubscribeAll()

        soundGameOver.start()
    }

    private fun onTouchPressed(touchedSquare: Square) {
        touchedPiece ?: return

        val move = Move(touchedPiece!!.square, touchedSquare, hero)
        if (!heroMadeMove(touchedSquare) || !isLegalMove(move)) {
            resetVisuals()
        }
    }

    private fun onTouchDragged(event: MotionEvent, touchedSquare: Square) {
        movingPiece?.let {
            it.x = event.x
            it.y = event.y
        }

        invalidate() // calls onDraw
    }

    // TODO: This function is ugly. Think how to split it into different functions
    private fun onTouchReleased(touchedSquare: Square) {
        touchedPiece?.let {
            val touchedMove = getTouchedMove(touchedSquare)
            if (touchedMove != null && heroMadeMove(touchedSquare) && isLegalMove(touchedMove)) {
                applyMove(touchedMove)
                publisher.notifySubscribers(MoveEvent(touchedMove))
            }

            return
        }

        touchedPiece = getTouchedPiece(touchedSquare)
        if (touchedPiece != null) {
            touchedPieceAvailableMoves = getAvailableMoves(touchedPiece!!)
        }
    }

    private fun getTouchedPiece(touchedSquare: Square): Piece? {
        val touchedPiece = game.board.pieceAt(touchedSquare)

        // If the touched piece is not of the current player - display nothing
        if (touchedPiece == null || game.currentPlayer != touchedPiece.player) {
            return null
        }

        return touchedPiece
    }

    private fun getTouchedMove(touchedSquare: Square): Move? {
        val move = Move(touchedPiece!!.square, touchedSquare, hero)
        if (move !in touchedPieceAvailableMoves) {
            return null
        }

        return touchedPieceAvailableMoves[move]!!
    }

    // getSquareTouched returns the square touched by the position in the MotionEvent
    private fun getTouchedSquare(event: MotionEvent): Square {
        var touchedSquare = getSquareFromMoveEvent(event)

        if (hero.isBlack()) {
            // When the player is black the screen is flipped vertically
            touchedSquare = touchedSquare.flipVertically(game.size)
        }

        return touchedSquare
    }

    private fun getSquareFromMoveEvent(event: MotionEvent): Square {
        val touchedColumn = (event.x / squareSize).toInt()
        val touchedRow = (game.size - 1) - (event.y / squareSize).toInt()

        return Square(touchedColumn, touchedRow)
    }

    // --- View Game Logic --------------------------------------------------------------------- \\

    fun select(mode: String, gameID: String = "") {
        setMode(mode)
        publisher.notifySubscribers(GameSelectedEvent(this, gameID))
    }

    fun startGame() {
        val gameStartedEvent = GameStartedEvent(this)
        publisher.notifySubscribers(gameStartedEvent)

        game.started = true
        game.subscribe(this)
    }

    fun applyMove(move: Move) {
        game.applyMove(move)
        soundMove.start()

        game.switchTurn()
        if (game.isOver()) {
            onGameOver()
        }

        resetVisuals()
    }

    private fun getAvailableMoves(piece: Piece): Map<Move, Move> {
        return game.getLegalMovesForPiece(piece).associateWith { it }
    }

    // heroMadeMove returns true if the player made a move inside of the board
    private fun heroMadeMove(touchedSquare: Square): Boolean {
        touchedPiece ?: return false

        return touchedPiece!!.player == game.currentPlayer &&
                touchedPiece!!.square != touchedSquare
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

    // isLegalMove returns true if the move is legal
    private fun isLegalMove(move: Move): Boolean {
        return move in touchedPieceAvailableMoves
    }

    // --- General ----------------------------------------------------------------------------- \\
    private fun setMode(mode: String) {
        chessDrawer.mode = mode
        chessGameListener = CHESS_GAME_LISTENER[mode]
    }
    fun setGameHero(hero: Player) {
        this.hero = hero
        this.chessDrawer.hero = hero
    }
}

data class MovingPiece(val piece: Piece, var x: Float, var y: Float, val bitmap: Bitmap, var player: Player)