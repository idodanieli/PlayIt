package com.idodanieli.playit.games.chess.ui.threat_visualizers

import com.idodanieli.playit.games.chess.ui.ChessView

class VisualizerCollection(vararg visualizers: EventVisualizer): EventVisualizer {
    private val visualizerList = visualizers.toList()

    override fun visualize(chessView: ChessView) {
        for (visualizer in visualizerList) {
            visualizer.visualize(chessView)
        }
    }
}