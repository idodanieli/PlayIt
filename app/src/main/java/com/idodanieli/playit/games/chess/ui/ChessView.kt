package com.idodanieli.playit.games.chess.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.MODE_TO_GAME_SUBSCRIBER
import com.idodanieli.playit.games.chess.MODE_DEFAULT
import com.idodanieli.playit.games.chess.MODE_ONLINE
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.*
import com.idodanieli.playit.games.chess.ui.threat_visualizers.AvailableMovesTouchVisualizer
import kotlin.math.min

class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs), GameSubscriber {
    // --- For Drawing -----------------------------------------------------------------------------
    private val chessDrawer = ChessDrawer(CHESSBOARD_SIZE, MODE_DEFAULT, context!!)
    private var touchData: TouchData? = null
    private var movingPiece: MovingPiece? = null
    private var squareSize = 0f

    // --- For Sounds ------------------------------------------------------------------------------
    private val soundMove = MediaPlayer.create(context, R.raw.sound_chess_move)
    private val soundGameOver = MediaPlayer.create(context, R.raw.sound_game_over)

    // --- For Logic -------------------------------------------------------------------------------
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

    fun setPlayers(hero: String, opponent: String) {
        heroTextView.text = hero
        opponentTextView.text = opponent

        heroTextView.visibility = VISIBLE
        opponentTextView.visibility = VISIBLE
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
        touchData ?: return

        touchData!!.piece.visualize(touchData, chessDrawer)

        if (touchData!!.isPreviewAbilityTouch()) {
            chessDrawer.drawAbilitySquare(touchData!!.square)
        } else {
            chessDrawer.drawTouchedSquare(touchData!!.square)
        }
    }

    private fun resetVisuals() {
        movingPiece = null
        touchData = null

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
        touchData ?: return

        if (touchedPieceAgain(touchedSquare)) {
            return
        }

        val move = Move(touchData!!.square, touchedSquare)
        if (!isLegalMove(move)) {
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

    private fun onTouchReleased(touchedSquare: Square) {
        if (heroTouchedPiece()) {
            onTouchedPiece(touchedSquare)
            return
        }

        touchData = createTouchData(touchedSquare)
    }

    private fun onTouchedPiece(touchedSquare: Square) {
        if (touchedPieceAgain(touchedSquare)) {
            touchData!!.touches++
        }

        val touchedMove = getTouchedMove(touchedSquare)
        if (isLegalMove(touchedMove)) {
            applyMove(touchedMove!!)
        }
    }

    private fun createTouchData(touchedSquare: Square): TouchData? {
        val touchedPiece = getTouchedPiece(touchedSquare)
        if (touchedPiece != null) {
            return TouchData(touchedSquare, touchedPiece, getAvailableMoves(touchedPiece))
        }

        return null
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
        touchData ?: return null

        val move = Move(touchData!!.square, touchedSquare)
        if (move !in touchData!!.availableMoves) {
            return null
        }

        return touchData!!.availableMoves[move]!!
    }

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

    private fun touchedPieceAgain(touchedSquare: Square): Boolean {
        touchData?.let {
            return it.square == touchedSquare
        }

        return false
    }

    // --- View Game Logic --------------------------------------------------------------------- \\

    fun select(mode: String, gameID: String = "") {
        MODE_TO_GAME_SUBSCRIBER[mode]?.let {
            subscribe(it)
        }

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

    private fun heroTouchedPiece(): Boolean {
        touchData ?: return false

        return touchData!!.piece.player == game.currentPlayer
    }

    private fun canHeroPlay(): Boolean {
        if ( getMode() == MODE_ONLINE ) {
            return game.currentPlayer == hero
        }

        return true
    }

    fun isOpponentsTurn(): Boolean {
        return game.currentPlayer != hero
    }

    fun isOpponentsMove(move: Move): Boolean {
        game.board.pieceAt(move.origin)?.let {
            return it.player != hero
        }

        game.board.pieceAt(move.dest)?.let {
            return it.player != hero
        }

        throw Exception("Invalid move checked! $move\n" +
                "${game.board}\n")
    }

    private fun isLegalMove(move: Move?): Boolean {
        move ?: return false

        return move in touchData!!.availableMoves
    }

    // --- General ---------------------------------------------------------------------------------
    private fun getMode(): String {
        return chessDrawer.mode
    }
    private fun setMode(mode: String) {
        chessDrawer.mode = mode
    }
    fun setGameHero(hero: Player) {
        this.hero = hero
        this.chessDrawer.hero = hero
    }
}

data class MovingPiece(val piece: Piece, var x: Float, var y: Float, val bitmap: Bitmap, var player: Player)