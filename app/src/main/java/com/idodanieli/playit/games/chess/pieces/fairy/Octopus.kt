package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.Compound

class Octopus(square: Square, player: Player) : Compound(square, player) {
    companion object {
        const val TYPE = "D"
    }

    override val type = TYPE

    // . . . . . . . .
    // . . . X . . . .
    // . X . . . X . .
    // . . . . . . . .
    // X . . D . . X .
    // . . . . . . . .
    // . X . . . X . .
    // . . . X . . . .
    override val pieces = listOf(
        Elephant(square, player),
        ThreeLeaper(square, player)
    )

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Octopus(square, player)
    }
}