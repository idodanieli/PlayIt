package com.idodanieli.playit.games.chess.ui.drawers

import android.animation.ValueAnimator
import com.idodanieli.playit.games.chess.logic.BoardDimensions
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.ui.ChessView

class MoveAnimator(duration: Long, dimensions: BoardDimensions): Drawer(dimensions) {
    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
    var animation: MoveAnimation? = null

    init {
        valueAnimator.duration = duration
    }

    fun animatePieceMovement(chessView: ChessView, move: Move) {
        val piece = chessView.game.board.pieceAt(move.origin)!!
        val origin = convertSquareToRectF(move.origin)
        val dest = convertSquareToRectF(move.dest)

        animation = MoveAnimation(piece, origin)

        val anim = ValueAnimator.ofFloat(0f, 1f)
        anim.duration = 1000

        anim.addUpdateListener { animation ->
            val fraction = animation.animatedFraction

            val diff = dest.subtract(origin)
            this.animation!!.rectF = origin.add( diff.multiply(fraction) )

            chessView.invalidate()
        }

        anim.start()
    }

    fun isAnimating(): Boolean {
        return this.animation != null
    }
}
