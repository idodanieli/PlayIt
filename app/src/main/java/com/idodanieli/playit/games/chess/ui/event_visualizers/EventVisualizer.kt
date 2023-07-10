package com.idodanieli.playit.games.chess.ui.event_visualizers

import com.idodanieli.playit.games.chess.ui.views.ChessView

interface EventVisualizer {
    fun visualize(chessView: ChessView)
}