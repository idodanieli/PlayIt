package com.idodanieli.playit.games.chess.game_listener

import com.idodanieli.playit.games.chess.ui.ChessView

interface ChessGameListener {
    fun canHeroPlay(chessView: ChessView): Boolean
}