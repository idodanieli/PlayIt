package com.idodanieli.playit.games.chess.game_listener

import android.util.Log
import com.idodanieli.playit.clients.GameClient
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.ui.ChessView

object LocalChessGameListener: ChessGameListener {

    override fun onPieceMoved(move: Move) {
        // Nothing
    }

    override fun onTurnSwitched(chessview: ChessView) {
        // Nothing
    }

}