package com.idodanieli.playit.games.chess.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.game_subscriber.GameEvent
import com.idodanieli.playit.games.chess.game_subscriber.GameSubscriber
import com.idodanieli.playit.games.chess.logic.Player


class PlayerView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs), GameSubscriber {
    lateinit var usernameTextView: TextView
    lateinit var capturedPieces: CapturedPiecesView
    lateinit var timer: TimerView

    // --- Initialization --------------------------------------------------------------------------
    init {
        LayoutInflater.from(context).inflate(R.layout.player_view, this, true)

        findViews()
    }

    private fun findViews() {
        usernameTextView = findViewById(R.id.playerName)
        capturedPieces = findViewById(R.id.capturedPieces)
        timer = findViewById(R.id.playerTimer)
    }

    // --- Core ------------------------------------------------------------------------------------
    fun setPlayer(username: String, color: Player) {
        usernameTextView.text = username
        usernameTextView.visibility = View.VISIBLE

        capturedPieces.player = color

        timer.player = color
        timer.visibility = View.VISIBLE
    }

    // --- General ---------------------------------------------------------------------------------
    override fun onGameEvent(event: GameEvent) {
        val components = listOf(usernameTextView, capturedPieces, timer)

        for (component in components) {
            if (component is GameSubscriber) {
                component.onGameEvent(event)
            }
        }
    }

    fun clear() {
        capturedPieces.clear()
    }
}