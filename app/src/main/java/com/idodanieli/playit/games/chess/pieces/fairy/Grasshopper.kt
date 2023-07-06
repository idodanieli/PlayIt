package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.ALL_DIRECTIONS
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.getFirstPieceInDirection
import com.idodanieli.playit.games.chess.ui.PieceDrawer

// The grasshopper is a fairy chess piece that moves along ranks, files, and diagonals (as a queen)
// but only by hopping over another piece. The piece to be hopped may be any distance away,
// but the grasshopper must land on the square immediately behind it in the same direction.
// If there is no piece to hop over, it cannot move.
// If the square beyond a piece is occupied by a piece of the opposite color, the grasshopper can capture that piece.
// The grasshopper may jump over pieces of either color; the piece being jumped over is unaffected.
class Grasshopper(square: Square, player: Player) : BasePiece(square, player) {
    companion object {
        const val TYPE = "H"

        init {
            PieceDrawer.addPiecePicture(TYPE, Player.WHITE, R.drawable.grasshopper_white)
            PieceDrawer.addPiecePicture(TYPE, Player.BLACK, R.drawable.grasshopper_black)
        }
    }

    override val type = TYPE

    override fun availableSquares(board: Board): List<Square> {
        return ALL_DIRECTIONS.mapNotNull { getAvailableSquareInDirection(board, it.value) }
    }

    private fun getAvailableSquareInDirection(board: Board, direction: Square): Square? {
        val firstPieceInDirection = getFirstPieceInDirection(this, board, direction) ?: return null
        val nextSquareInDirection = firstPieceInDirection.square + direction

        if ( !board.isIn(nextSquareInDirection) ||
              board.playerAt(nextSquareInDirection) == this.player ) {
            return null
        }

        return nextSquareInDirection
    }

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Grasshopper(square, player)
    }
}