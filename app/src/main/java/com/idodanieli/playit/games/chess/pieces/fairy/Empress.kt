package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.pieces.core.Compound
import com.idodanieli.playit.games.chess.pieces.classic.Rook
import com.idodanieli.playit.games.chess.ui.PieceDrawer


const val TYPE_EMPRESS = "M"

class Empress(square: Square, player: Player) : Compound(square, player) {
    companion object {
        init {
            PieceDrawer.addPiecePicture(TYPE_EMPRESS, Player.WHITE, R.drawable.empress_white)
            PieceDrawer.addPiecePicture(TYPE_EMPRESS, Player.BLACK, R.drawable.empress_black)
        }
    }

    override val type = TYPE_EMPRESS
    override val pieces = listOf(
        Knight(square, player),
        Rook(square, player)
    )

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Empress(square, player)
    }
}