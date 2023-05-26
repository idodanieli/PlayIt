package com.idodanieli.playit.games.chess.game_listener

import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.ui.ChessView

interface ChessGameListener {
    fun onGameStarted()
    fun onPieceMoved(move: Move)
    fun onTurnSwitched(chessview: ChessView)
}