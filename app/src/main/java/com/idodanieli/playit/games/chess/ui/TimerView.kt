package com.idodanieli.playit.games.chess.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.idodanieli.playit.games.chess.MODE_DEFAULT
import com.idodanieli.playit.games.chess.game_subscriber.GameEvent
import com.idodanieli.playit.games.chess.game_subscriber.GameSubscriber
import com.idodanieli.playit.games.chess.game_subscriber.PieceCapturedEvent
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.drawers.PieceDrawer


class TimerView(context: Context?, attrs: AttributeSet?): androidx.appcompat.widget.AppCompatTextView(context!!, attrs), GameSubscriber {
    override fun onGameEvent(event: GameEvent) {
        when (event) {

        }
    }

}