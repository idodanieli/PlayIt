package com.idodanieli.playit.games.chess.ui.drawers

import android.animation.ValueAnimator
import android.graphics.RectF
import com.idodanieli.playit.games.chess.logic.BoardDimensions
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.ui.ChessView

class MoveAnimator(private val duration: Long, dimensions: BoardDimensions): Drawer(dimensions) {
    var animation: MoveAnimation? = null


    fun animatePieceMovement(chessView: ChessView, move: Move) {
        val piece = chessView.game.board.pieceAt(move.origin) ?: return

        val origin = convertSquareToRectFAccordingToHero(move.origin, chessView.hero)
        val dest = convertSquareToRectFAccordingToHero(move.dest, chessView.hero)

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

    private fun createValueAnimator(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = duration

        return valueAnimator
    }
}
