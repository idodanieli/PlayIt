package com.idodanieli.playit.games.chess.game_listener

import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.ui.ChessView

interface ChessGameListener {
    fun onGameStarted(chessView: ChessView, gameID: String)
    fun onGameOver()
    fun onPieceMoved(move: Move)
    fun canHeroPlay(chessView: ChessView): Boolean
}