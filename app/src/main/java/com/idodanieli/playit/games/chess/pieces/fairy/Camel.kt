package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.King
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.ui.ChessDrawer

private val MOVE_OFFSETS = arrayOf(1, 3, -1, -3)
const val TYPE_CAMEL = "C"

// The camel or long knight is a fairy chess piece with an elongated knight move.
// It can jump three squares horizontally and one square vertically or three squares vertically and one square horizontally,
// regardless of intervening pieces. Therefore, it is a (1,3)-leaper.
class Camel(square: Square, player: Player) : Knight(square, player) {
    companion object {
        init {
            ChessDrawer.addPiecePicture(TYPE_CAMEL, Player.WHITE, R.drawable.camel_white)
            ChessDrawer.addPiecePicture(TYPE_CAMEL, Player.BLACK, R.drawable.camel_black)
        }
    }

    override val moveOffsets: Array<Int> = MOVE_OFFSETS
    override val type = TYPE_CAMEL

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Camel(square, player)
    }
}