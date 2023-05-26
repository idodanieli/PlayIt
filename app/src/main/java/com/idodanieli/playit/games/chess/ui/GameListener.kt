package com.idodanieli.playit.games.chess.ui

import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player

interface GameListener {
    fun onPieceMoved(move: Move)
    fun onTurnSwitched(chessview: ChessView)
    fun onGameOver(winner: Player)
}