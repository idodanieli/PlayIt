package com.idodanieli.playit.games.chess.ui.threat_visualizers

import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.TouchData

class VisualizerCollection(vararg visualizers: TouchVisualizer): TouchVisualizer {
    private val visualizerList = visualizers.toList()

    override fun visualize(touch: TouchData?, chessView: ChessView) {
        touch ?: return

        for (visualizer in visualizerList) {
            visualizer.visualize(touch, chessView)
        }
    }
}