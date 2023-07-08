package com.idodanieli.playit.games.chess.ui.drawers

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.RectF
import com.idodanieli.playit.games.chess.logic.BoardDimensions
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.ChessView

class MoveAnimator(private val duration: Long, private val pieceDrawer: PieceDrawer) {
    var animation: MoveAnimation? = null

    // --- Drawing ---------------------------------------------------------------------------------
    fun draw() {
        animation?.let {
            drawAnimation(it)
        }
    }

    private fun drawAnimation(animation: MoveAnimation) {
        pieceDrawer.drawPieceAtRect(animation.piece, animation.rectF)
    }

    // --- Animating -------------------------------------------------------------------------------
    fun animatePieceMovement(chessView: ChessView, move: Move) {
        val piece = chessView.game.board.pieceAt(move.origin) ?: return

        val origin = pieceDrawer.convertSquareToRectFAccordingToHero(move.origin, chessView.hero)
        val dest = pieceDrawer.convertSquareToRectFAccordingToHero(move.dest, chessView.hero)

        animation = MoveAnimation(piece, origin)

        animate(chessView, origin, dest)
    }

    private fun animate(chessView: ChessView, origin: RectF, dest: RectF) {
        val createValueAnimator = createValueAnimator()

        createValueAnimator.addUpdateListener { animation ->
            val fraction = animation.animatedFraction

            val diff = dest.subtract(origin)
            this.animation!!.rectF = origin.add( diff.multiply(fraction) )

            chessView.invalidate()
        }

        createValueAnimator.start()
    }

    fun isAnimating(): Boolean {
        return this.animation != null
    }

    fun isPieceBeingAnimated(piece: Piece): Boolean {
        animation ?: return false

        return piece == animation!!.piece
    }

    private fun createValueAnimator(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = duration

        return valueAnimator
    }
}
