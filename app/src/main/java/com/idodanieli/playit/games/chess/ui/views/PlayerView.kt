package com.idodanieli.playit.games.chess.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.game_subscriber.GameEvent
import com.idodanieli.playit.games.chess.game_subscriber.GameSubscriber
import com.idodanieli.playit.games.chess.logic.Player


class PlayerView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs), GameSubscriber {
    lateinit var usernameTextView: TextView
    lateinit var capturedPieces: CapturedPiecesView
    lateinit var timer: TimerView
    lateinit var profilePicture: ImageView

    // --- Initialization --------------------------------------------------------------------------
    init {
        LayoutInflater.from(context).inflate(R.layout.player_view, this, true)

        findViews()
    }

    private fun findViews() {
        usernameTextView = findViewById(R.id.playerName)
        capturedPieces = findViewById(R.id.capturedPieces)
        timer = findViewById(R.id.playerTimer)
        profilePicture = findViewById(R.id.profilePic)
    }

    // --- Core ------------------------------------------------------------------------------------
    fun setPlayer(username: String, color: Player) {
        usernameTextView.text = username

        timer.player = color
        capturedPieces.player = color

        timer.visibility = View.VISIBLE
        profilePicture.visibility = View.VISIBLE
        usernameTextView.visibility = View.VISIBLE
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
        timer.clear()
    }
}