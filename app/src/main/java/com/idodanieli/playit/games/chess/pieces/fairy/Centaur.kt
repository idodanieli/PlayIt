package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.pieces.core.Compound
import com.idodanieli.playit.games.chess.ui.ChessDrawer


const val TYPE_CENTAUR = "S"

class Centaur(square: Square, player: Player) : Compound(square, player) {
    companion object {
        init {
            ChessDrawer.addPiecePicture(TYPE_CENTAUR, Player.WHITE, R.drawable.centaur_white)
            ChessDrawer.addPiecePicture(TYPE_CENTAUR, Player.BLACK, R.drawable.centaur_black)
        }
    }

    override val type = TYPE_CENTAUR
    override val pieces = listOf(
        Knight(square, player),
        Man(square, player)
    )

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Centaur(square, player)
    }
}