package com.idodanieli.playit.games.chess.ui

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Handler
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.game_subscriber.*


class Speaker(val context: Context): GameSubscriber {

    companion object {
        private const val DEFAULT_MOVE_SOUND_DELAY_MILLISECONDS = 10L
    }

    override fun onGameEvent(event: GameEvent) {
        when (event) {
            is MoveEvent -> playMoveSound()
            is PieceCapturedEvent -> playCaptureSound()
            is GameOverEvent -> playGameOverSound()
            is CheckEvent -> playCheckSound()
        }
    }

    // This logic is here so that we wont play the move sound if there is another event occurring
    private var playMoveSoundRunnable: Runnable? = Runnable { playSound(R.raw.sound_chess_move) }
    private val handler = Handler(context.mainLooper)

    private fun playMoveSound() {
        playMoveSoundRunnable?.let {
            handler.postDelayed(it, DEFAULT_MOVE_SOUND_DELAY_MILLISECONDS)
            return
        }

        playMoveSoundRunnable = Runnable { playSound(R.raw.sound_chess_move) }
    }
    
    private fun playCaptureSound() {
        removePlayMoveSoundCall()
        playSound(R.raw.sound_capture)
    }
    
    private fun playCheckSound() {
        removePlayMoveSoundCall()
        playSound(R.raw.sound_check)
    }

    private fun playGameOverSound() {
        playSound(R.raw.sound_game_over)
    }

    private fun removePlayMoveSoundCall() {
        playMoveSoundRunnable?.let {
            handler.removeCallbacks(it)
            playMoveSoundRunnable = null
        }

    }

    private fun playSound(resource: Int) {
        val mp = MediaPlayer.create(context, resource)
        mp.setOnCompletionListener { onCompletionListener }

        mp.start()
    }

    private val onCompletionListener = OnCompletionListener {
        it.reset()
        it.release()
    }
}