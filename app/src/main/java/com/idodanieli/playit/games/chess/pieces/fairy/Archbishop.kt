package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.Compound
import com.idodanieli.playit.games.chess.pieces.classic.Bishop
import com.idodanieli.playit.games.chess.pieces.classic.King
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.ui.ChessDrawer


const val TYPE_ARCHBISHOP = "X"

class Archbishop(square: Square, player: Player) : Compound(square, player) {
    companion object {
        init {
            ChessDrawer.addPiecePicture(TYPE_ARCHBISHOP, Player.WHITE, R.drawable.archbishop_white)
            ChessDrawer.addPiecePicture(TYPE_ARCHBISHOP, Player.BLACK, R.drawable.archbishop_black)
        }
    }

    override val type = TYPE_ARCHBISHOP
    override val pieces = listOf(
        Knight(square, player),
        Bishop(square, player)
    )

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Archbishop(square, player)
    }
}