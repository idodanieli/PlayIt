package com.idodanieli.playit.games.chess.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.idodanieli.playit.activities.openPiecePreviewActivity
import com.idodanieli.playit.games.chess.MODE_TO_GAME_SUBSCRIBER
import com.idodanieli.playit.games.chess.MODE_DEFAULT
import com.idodanieli.playit.games.chess.MODE_ONLINE
import com.idodanieli.playit.games.chess.game_subscriber.*
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.ui.drawers.*
import com.idodanieli.playit.games.chess.ui.event_visualizers.LastMoveVisualizer
import com.idodanieli.playit.games.chess.ui.event_visualizers.TouchedSquareVisualizer
import com.idodanieli.playit.games.chess.ui.event_visualizers.VisualizerCollection
import com.idodanieli.playit.games.chess.variants.*

@SuppressLint("ClickableViewAccessibility")
class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var focusedPiece: TouchData? = null
    var currentTouch: TouchData? = null

    val publisher = Publisher()

    var hero = Player.WHITE

    private val visualizers: VisualizerCollection = VisualizerCollection(LastMoveVisualizer(), TouchedSquareVisualizer())
    lateinit var chessDrawer: ChessDrawer
    lateinit var game: Game

    // --- onDraw ----------------------------------------------------------------------------------
    // onDraw is called everytime invalidate() is called
    // the order of the draw functions inside is crucial
    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val squareSize = width / game.board.dimensions.cols.toFloat()
        chessDrawer.initialize(canvas, squareSize)
        chessDrawer.drawChessboard()

        visualizeGameEvents()

        chessDrawer.drawPieces(game)

        chessDrawer.movingPiece?.let { it ->
            chessDrawer.drawPieceAtRect(it.piece, it.rectF)
        }
    }

    private fun animatePieceMovement(move: Move) {
        val piece = game.board.pieceAt(move.origin)!!
        val origin = chessDrawer.convertSquareToRectF(move.origin)
        val dest = chessDrawer.convertSquareToRectF(move.dest)

        chessDrawer.movingPiece = MovingPiece(piece, origin)

        val anim = ValueAnimator.ofFloat(0f, 1f)
        anim.duration = 1000

        anim.addUpdateListener { animation ->
            val fraction = animation.animatedFraction

            val diff = dest.subtract(origin)
            chessDrawer.movingPiece!!.rectF = origin.add( diff.multiply(fraction) )
            invalidate()
        }

        anim.start()
    }

    private fun visualizeGameEvents() {
        visualizers.visualize(this)

        focusedPiece?.piece?.visualize(this)
    }

    // --- OnTouch ---------------------------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        if (!game.started) { return true }

        val touchedSquare = getTouchedSquare(event)
        currentTouch = getTouchData(game, touchedSquare)

        when (event.action) {
            // This action occurs when the user initially presses down on the screen
            MotionEvent.ACTION_DOWN -> {
                handler.postDelayed(onLongTouch, 250)

                resetUIOnIllegalMove(touchedSquare)
            }

            // This action occurs when the user moves their finger on the screen after pressing down.
            MotionEvent.ACTION_MOVE -> {
                // onTouchMove(event, touchedSquare)
            }

            //  This action occurs when the user releases their finger or lifts the stylus from the screen
            MotionEvent.ACTION_UP -> {
                handler.removeCallbacks(onLongTouch)

                onTouchReleased(touchedSquare)
                invalidate()
            }
        }

        return true
    }

    private val onLongTouch = Runnable {
        // TODO: Send event to another component onLongScreenTouch? ( So we could delete currentTouch... )
        currentTouch?.let {
            context!!.openPiecePreviewActivity(it.piece)
        }
    }

    fun onTouchReleased(touchedSquare: Square) {
        if (heroIsPlaying()) {
            play(focusedPiece, touchedSquare)
            return
        }

        if (canHeroPlay()) {
            focusedPiece = getHeroTouchData(game, touchedSquare)
        }
    }

    private fun play(firstTouch: TouchData?, secondTouchedSquare: Square) {
        firstTouch?.let { touch ->
            val touchedMove = touch.getMove(secondTouchedSquare)

            if (touch.equals(secondTouchedSquare)) {
                touch.touches++
            }

            when {
                touch.isActivateAbilityTouch() -> applyAbilityMove(touchedMove)

                isLegalMove(touchedMove) -> applyMove(touchedMove)
            }
        }
    }

    private fun getTouchedSquare(event: MotionEvent): Square {
        var touchedSquare = getTouchedSquareFromMotionEvent(event)

        if (hero.isBlack()) {
            // When the player is black the screen is flipped vertically
            touchedSquare = touchedSquare.flipVertically(game.board.dimensions.rows)
        }

        return touchedSquare
    }

    private fun getTouchedSquareFromMotionEvent(event: MotionEvent): Square {
        val touchedColumn = (event.x / chessDrawer.squareSize).toInt()
        val touchedRow = (game.board.dimensions.rows - 1) - (event.y / chessDrawer.squareSize).toInt()

        return Square(touchedColumn, touchedRow)
    }

    private fun resetUIOnIllegalMove(touchedSquare: Square) {
        if ( focusedPiece == null || focusedPiece!!.equals(touchedSquare)) {
            return
        }

        val move = Move(focusedPiece!!.square, touchedSquare)
        if (!isLegalMove(move)) {
            resetUI()
        }
    }

    private fun resetUI() {
        focusedPiece = null
        invalidate()
    }

    // --- View Game Logic -------------------------------------------------------------------------
    fun select(mode: String, gameID: String = "") {
        setMode(mode)
        publisher.notifySubscribers(GameSelectedEvent(this, gameID))
    }

    fun startGame() {
        val gameStartedEvent = GameStartedEvent(this)
        publisher.notifySubscribers(gameStartedEvent)

        game.started = true
    }

    fun applyMove(move: Move) {
        animatePieceMovement(move)
        game.applyMove(move)
        afterMove()
    }

    fun applyAbilityMove(move: Move) {
        game.applyAbilityMove(move)

        afterMove()
    }

    private fun afterMove() {
        game.switchTurn()
        if (game.isOver()) {
            onGameOver()
        } else if (game.isStalemate()) {
            onStalemate()
        }

        resetUI()
    }

    private fun heroIsPlaying(): Boolean {
        focusedPiece ?: return false

        return focusedPiece!!.piece.player == game.currentPlayer
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

    private fun isLegalMove(move: Move?): Boolean {
        move ?: return false

        return move in focusedPiece!!.availableMoves
    }

    // --- Game Over -------------------------------------------------------------------------------
    private fun onGameOver() {
        val winner = game.currentPlayer.opposite()
        this.close(winner)
    }

    private fun onStalemate() {
        this.close(null)
    }

    private fun close(winner: Player?) {
        val gameOverEvent = GameOverEvent(winner, hero)

        game.notifySubscribers(gameOverEvent)
        clear()
    }

    // --- Publisher -------------------------------------------------------------------------------
    fun subscribe(subscriber: GameSubscriber) {
        publisher.subscribe(subscriber)
        game.subscribe(subscriber)
    }

    fun subscribeVisualizers() {
        for (visualizer in visualizers) {
            if (visualizer is GameSubscriber) {
                subscribe(visualizer)
            }
        }
    }

    fun clear() {
        publisher.unsubscribeAll()

        if(::game.isInitialized) {
            game.unsubscribeAll()
        }

        visualizers.clear()

        currentTouch = null
        focusedPiece = null
    }

    // --- General ---------------------------------------------------------------------------------
    private fun getMode(): String {
        return chessDrawer.mode
    }
    private fun setMode(mode: String) {
        chessDrawer.mode = mode

        MODE_TO_GAME_SUBSCRIBER[mode]?.let {
            subscribe(it)
        }
    }
    fun setGameHero(hero: Player) {
        this.hero = hero
        this.chessDrawer.hero = hero
    }

    @JvmName("setGame1")
    fun setGame(game: Game) {
        this.game = game
        this.chessDrawer = ChessDrawer(game.board.dimensions, MODE_DEFAULT, context!!)
    }
}