package com.idodanieli.playit.games.chess.ui.event_visualizers

import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.utils.Clearable

class VisualizerCollection(vararg visualizers: EventVisualizer): EventVisualizer, Clearable {
    private val visualizerList = visualizers.toList()

    override fun visualize(chessView: ChessView) {
        for (visualizer in visualizerList) {
            visualizer.visualize(chessView)
        }
    }

    operator fun iterator(): Iterator<EventVisualizer> {
        return visualizerList.iterator()
    }

    override fun clear() {
        for (visualizer in visualizerList) {
            if (visualizer is Clearable) {
                visualizer.clear()
            }
        }
    }
}