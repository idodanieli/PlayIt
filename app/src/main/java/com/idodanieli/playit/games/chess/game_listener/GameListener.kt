package com.idodanieli.playit.games.chess.game_listener

import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.ui.ChessView

interface GameListener {
    fun onGameSelected(chessView: ChessView, gameID: String)
    fun onGameOver(winner: Player)
}