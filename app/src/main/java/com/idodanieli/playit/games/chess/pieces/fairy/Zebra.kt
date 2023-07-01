package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.ui.PieceDrawer

private val MOVE_OFFSETS = arrayOf(2, 3, -2, -3)
const val TYPE_ZEBRA = "Z"

class Zebra(square: Square, player: Player) : Knight(square, player) {
    companion object {
        init {
            PieceDrawer.addPiecePicture(TYPE_ZEBRA, Player.WHITE, R.drawable.zebra_white)
            PieceDrawer.addPiecePicture(TYPE_ZEBRA, Player.BLACK, R.drawable.zebra_black)
        }
    }

    override val moveOffsets: Array<Int> = MOVE_OFFSETS
    override val type = TYPE_ZEBRA

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Zebra(square, player)
    }
}