package com.idodanieli.playit.games.chess.ui

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import com.idodanieli.playit.games.chess.game_subscriber.GameEvent
import com.idodanieli.playit.games.chess.game_subscriber.GameStartedEvent
import com.idodanieli.playit.games.chess.game_subscriber.GameSubscriber
import com.idodanieli.playit.games.chess.game_subscriber.TurnSwitched
import com.idodanieli.playit.games.chess.logic.Player
import java.util.concurrent.TimeUnit


class TimerView(context: Context?, attrs: AttributeSet?): androidx.appcompat.widget.AppCompatTextView(context!!, attrs), GameSubscriber {
    companion object {
        private const val MILLISECOND = 1L
        private const val SECOND = 1000 * MILLISECOND // In Milliseconds
        private const val MINUTE = 60 * SECOND

        private const val DEFAULT_TIME_SPAN = 10 * MINUTE
        private const val DEFAULT_COUNTDOWN_INTERVAL = MILLISECOND

        private const val FINISHED_TIME = "00:00.000"
    }

    lateinit var player: Player

    private var timeLeftInMillis = DEFAULT_TIME_SPAN
    private var timer = createTimer()

    init {
        text = formatMilliseconds(timeLeftInMillis)
    }

    override fun onGameEvent(event: GameEvent) {
        when (event) {
            is GameStartedEvent -> startTimerIfHeroIsWhite()
            is TurnSwitched -> {
                if (event.currentPlayer == player) { timer.start() }
                else pauseTimer()
            }
        }
    }

    private fun startTimerIfHeroIsWhite() {
        if (player.isWhite()) {
            timer.start()
        }
    }

    private fun createTimer(): CountDownTimer {
        return  object : CountDownTimer(timeLeftInMillis, DEFAULT_COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                text = formatMilliseconds(millisUntilFinished)
            }

            override fun onFinish() {
                // TODO: Should Publish GameOverEvent
                text = FINISHED_TIME
            }
        }
    }
    private fun pauseTimer() {
        timer.cancel()
        timer = createTimer()
    }

    private fun formatMilliseconds(milliseconds: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60
        val millis = milliseconds % 1000

        return String.format("%02d:%02d", minutes, seconds)
        // return String.format("%02d:%02d.%03d", minutes, seconds, millis)
    }
}