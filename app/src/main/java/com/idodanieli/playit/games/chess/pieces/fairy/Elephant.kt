package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.DIAGONAL_DIRECTIONS
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.StaticHopper
import com.idodanieli.playit.games.chess.ui.PieceDrawer

const val TYPE_ELEPHANT = "E"

// Also known as Alfil
// Moves like this:
// . . . . . . . .
// . . . . . . . .
// . X . . . X . .
// . . . . . . . .
// . . . E . . . .
// . . . . . . . .
// . X . . . X . .
// . . . . . . . .
class Elephant(square: Square, player: Player) : StaticHopper(square, player) {
    companion object {
        init {
            PieceDrawer.addPiecePicture(TYPE_ELEPHANT, Player.WHITE, R.drawable.elephant_white)
            PieceDrawer.addPiecePicture(TYPE_ELEPHANT, Player.BLACK, R.drawable.elephant_black)
        }
    }

    override val directions: List<Square> = DIAGONAL_DIRECTIONS.map { it.value }
    override val hopSize: Int = 2
    override val type = TYPE_ELEPHANT

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Elephant(square, player)
    }
}