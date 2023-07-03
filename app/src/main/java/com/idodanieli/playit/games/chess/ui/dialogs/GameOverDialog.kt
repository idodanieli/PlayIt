package com.idodanieli.playit.games.chess.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.getRandomItem
import pl.droidsonroids.gif.GifImageView

class GameOverDialog(private val winner: Player?, private val hero: Player, private val onPositiveButton: Runnable): DialogFragment() {
    companion object {
        private val WIN_GIFS = listOf(R.drawable.game_over_win_clapping, R.drawable.game_over_win_mr_bean)
        private val LOSS_GIFS = listOf(R.drawable.game_over_loss_mario)
    }

    private lateinit var winnerTextView: TextView
    private lateinit var gameOverGifView: GifImageView
    private lateinit var positiveButton: Button
    private lateinit var negativeButton: Button

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parent = inflater.inflate(R.layout.dialog_game_over, container, false)

        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_round_corner_background)

        findViews(parent)
        setViews()

        return parent
    }

    private fun findViews(parent: View) {
        winnerTextView = parent.findViewById(R.id.winner)
        gameOverGifView = parent.findViewById(R.id.gameOverGif)

        positiveButton = parent.findViewById(R.id.gameOverPositiveButton)
        negativeButton = parent.findViewById(R.id.gameOverNegativeButton)
    }

    private fun setViews() {
        winnerTextView.text = if(heroWon()) "You Won!" else "You Lost 🙁"
        setGIF()

        positiveButton.setOnClickListener {
            onPositiveButton.run()
            this.dismiss()
        }
        negativeButton.setOnClickListener {
            this.dismiss()
        }
    }

    private fun setGIF() {
        if (winner == null) {
            // TODO: STALEMATE
            return
        }

        val gif = if (heroWon()) getRandomItem(WIN_GIFS) else getRandomItem(LOSS_GIFS)
        gameOverGifView.setImageResource(gif)
    }

    private fun heroWon(): Boolean {
        return hero == winner
    }
}