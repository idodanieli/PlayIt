package com.idodanieli.playit.games.chess.game_listener

import android.util.Log
import com.idodanieli.playit.clients.GameClient
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.ui.ChessView

object OnlineChessGameListener: ChessGameListener {
    override fun onPieceMoved(move: Move) {
        GameClient.getInstance().movePiece(move)
    }

    override fun onTurnSwitched(chessview: ChessView) {
        // TODO: For local play do nothing
        Thread.sleep(5) // TODO: Eliminate race condition
        val lastMove = GameClient.getInstance().getLastMove()
        Log.d("GameClient", "LastMove: $lastMove")
    }
}