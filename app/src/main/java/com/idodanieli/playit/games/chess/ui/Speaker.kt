package com.idodanieli.playit.games.chess.ui

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import com.idodanieli.playit.R

class Speaker {
    companion object {

        private val onCompletionListener = OnCompletionListener {
            it.reset()
            it.release()
        }

        fun playMoveSound(context: Context) {
            playSound(context, R.raw.sound_chess_move)
        }

        fun playGameOverSound(context: Context) {
            playSound(context, R.raw.sound_game_over)
        }

        private fun playSound(context: Context, resource: Int) {
            val mp = MediaPlayer.create(context, resource)
            mp.setOnCompletionListener { onCompletionListener }

            mp.start()
        }
    }
}