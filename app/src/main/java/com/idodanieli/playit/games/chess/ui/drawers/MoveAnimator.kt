package com.idodanieli.playit.games.chess.ui.drawers

import android.animation.Animator
import android.animation.ValueAnimator
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.ChessView

class MoveAnimator(private val duration: Long, private val pieceDrawer: PieceDrawer) {
    private var currentAnimations = listOf<MoveAnimation>()
    private var animating = false

    // --- Drawing ---------------------------------------------------------------------------------
    fun draw() {
        for (animation in currentAnimations) {
            drawAnimation(animation)
        }
    }

    private fun drawAnimation(animation: MoveAnimation) {
        pieceDrawer.drawPieceAtRect(animation.piece, animation.origin)
    }

    // --- Animating -------------------------------------------------------------------------------
    fun animateMove(chessView: ChessView, move: Move) {
        currentAnimations = createMoveAnimations(chessView, move)

        for (animation in currentAnimations) {
            animate(chessView, animation)
        }
    }

    private fun createMoveAnimations(chessView: ChessView, move: Move): List<MoveAnimation> {
        val animations = mutableListOf<MoveAnimation>()

        createMoveAnimation(chessView, move)?.let { animations.add(it) }

        for (m in move.followUpMoves) {
            createMoveAnimation(chessView, m)?.let { animations.add(it) }
        }

        return animations
    }
    private fun createMoveAnimation(chessView: ChessView, move: Move): MoveAnimation? {
        val piece = chessView.game.board.pieceAt(move.origin) ?: return null

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

        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                animating = true
            }

            override fun onAnimationEnd(animation: Animator?) {
                animating = false
                currentAnimations = emptyList()
            }

            override fun onAnimationCancel(animation: Animator?) {
                // Animation cancel
            }

            override fun onAnimationRepeat(animation: Animator?) {
                // Animation repeat
            }
        })

        return valueAnimator
    }

    fun isAnimating(): Boolean {
        return animating
    }

    fun isPieceBeingAnimated(piece: Piece): Boolean {
        return currentAnimations.any { it.piece == piece}
    }
}
