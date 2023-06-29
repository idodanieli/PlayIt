package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.King
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.pieces.core.Compound
import com.idodanieli.playit.games.chess.ui.ChessDrawer


const val TYPE_WILDBEAST = "W"

class WildBeast(square: Square, player: Player) : Compound(square, player) {
    companion object {
        init {
            ChessDrawer.addPiecePicture(TYPE_WILDBEAST, Player.WHITE, R.drawable.wildbeast_white)
            ChessDrawer.addPiecePicture(TYPE_WILDBEAST, Player.BLACK, R.drawable.wildbeast_black)
        }
    }

    override val type = TYPE_WILDBEAST
    override val pieces = listOf(
        Knight(square, player),
        Camel(square, player)
    )

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return WildBeast(square, player)
    }
}