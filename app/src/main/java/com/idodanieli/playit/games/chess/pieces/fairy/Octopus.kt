package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.Compound
import com.idodanieli.playit.games.chess.ui.ChessDrawer

class Octopus(square: Square, player: Player) : Compound(square, player) {
    companion object {
        const val TYPE = "D"

        init {
            ChessDrawer.addPiecePicture(TYPE, Player.WHITE, R.drawable.octopus_white)
            ChessDrawer.addPiecePicture(TYPE, Player.BLACK, R.drawable.octopus_black)
        }
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