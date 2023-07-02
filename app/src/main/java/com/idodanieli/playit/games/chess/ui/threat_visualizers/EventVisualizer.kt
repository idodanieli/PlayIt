package com.idodanieli.playit.games.chess.ui.threat_visualizers

import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.TouchData

interface EventVisualizer {
    fun visualize(chessView: ChessView)
}