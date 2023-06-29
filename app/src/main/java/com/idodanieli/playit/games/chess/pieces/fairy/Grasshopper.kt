package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.DIRECTIONS
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.King
import com.idodanieli.playit.games.chess.pieces.core.Hopper
import com.idodanieli.playit.games.chess.ui.ChessDrawer

const val TYPE_GRASSHOPPER = "H"

// The grasshopper is a fairy chess piece that moves along ranks, files, and diagonals (as a queen)
// but only by hopping over another piece. The piece to be hopped may be any distance away,
// but the grasshopper must land on the square immediately behind it in the same direction.
// If there is no piece to hop over, it cannot move.
// If the square beyond a piece is occupied by a piece of the opposite color, the grasshopper can capture that piece.
// The grasshopper may jump over pieces of either color; the piece being jumped over is unaffected.
class Grasshopper(square: Square, player: Player) : Hopper(square, player) {
    companion object {
        init {
            ChessDrawer.addPiecePicture(TYPE_GRASSHOPPER, Player.WHITE, R.drawable.grasshopper_white)
            ChessDrawer.addPiecePicture(TYPE_GRASSHOPPER, Player.BLACK, R.drawable.grasshopper_black)
        }
    }

    override val directions: List<Square> = DIRECTIONS.map { it.value }
    override val hopSize: Int = 3
    override val type = TYPE_GRASSHOPPER

    override fun availableSquares(board: Board): List<Square> {
        return super.availableSquares(board).filter { isAHopOverAPiece(it, board) }
    }

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Grasshopper(square, player)
    }
}