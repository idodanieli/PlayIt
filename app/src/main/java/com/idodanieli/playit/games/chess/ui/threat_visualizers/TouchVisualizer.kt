package com.idodanieli.playit.games.chess.ui.threat_visualizers

import com.idodanieli.playit.games.chess.ui.TouchData

interface TouchVisualizer {
    fun visualize(touch: TouchData?)
}