package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.ui.drawers.PieceDrawer

private val MOVE_OFFSETS = arrayOf(1, 4, -1, -4)
const val TYPE_GIRAFFE = "G"

class Giraffe(square: Square, player: Player) : Knight(square, player) {
    companion object {
        init {
            PieceDrawer.addPiecePicture(TYPE_GIRAFFE, Player.WHITE, R.drawable.giraffe_white)
            PieceDrawer.addPiecePicture(TYPE_GIRAFFE, Player.BLACK, R.drawable.giraffe_black)
        }
    }

    override val moveOffsets: Array<Int> = MOVE_OFFSETS
    override val type = TYPE_GIRAFFE

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Giraffe(square, player)
    }
}