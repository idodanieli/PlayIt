package com.idodanieli.playit.games.chess.ui.drawers

import android.animation.ValueAnimator
import android.graphics.RectF
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.ChessView

class MoveAnimator(private val duration: Long, private val pieceDrawer: PieceDrawer) {
    private var animation: MoveAnimation? = null

    // --- Drawing ---------------------------------------------------------------------------------
    fun draw() {
        animation?.let {
            drawAnimation(it)
        }
    }

    private fun drawAnimation(animation: MoveAnimation) {
        pieceDrawer.drawPieceAtRect(animation.piece, animation.origin)
    }

    // --- Animating -------------------------------------------------------------------------------
    fun animateMove(chessView: ChessView, move: Move) {
        val piece = chessView.game.board.pieceAt(move.origin) ?: return

        animation = createMoveAnimation(chessView, piece, move)

        animate(chessView, animation!!)
    }

    private fun createMoveAnimation(chessView: ChessView, piece: Piece, move: Move): MoveAnimation {
        val origin = pieceDrawer.convertSquareToRectFAccordingToHero(move.origin, chessView.hero)
        val dest = pieceDrawer.convertSquareToRectFAccordingToHero(move.dest, chessView.hero)

        return MoveAnimation(piece, origin, dest)
    }

    private fun animate(chessView: ChessView, animation: MoveAnimation) {
        val createValueAnimator = createValueAnimator()

        createValueAnimator.addUpdateListener { valueAnimator ->
            val fraction = valueAnimator.animatedFraction

            val diff = animation.dest.subtract(animation.origin)
            animation.origin = animation.origin.add( diff.multiply(fraction) )

            chessView.invalidate()
        }

        createValueAnimator.start()
    }

    private fun createValueAnimator(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = duration

        return valueAnimator
    }

    fun isAnimating(): Boolean {
        return this.animation != null
    }

    fun isPieceBeingAnimated(piece: Piece): Boolean {
        animation ?: return false

        return piece == animation!!.piece
    }
}
