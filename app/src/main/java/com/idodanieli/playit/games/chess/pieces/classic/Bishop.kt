package com.idodanieli.playit.games.chess.pieces.classic

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.NO_MAX_STEPS
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.Rider
import com.idodanieli.playit.games.chess.ui.PieceDrawer

private val moveOffsets = arrayOf(1, -1)
const val TYPE_BISHOP = "B"

class Bishop(square: Square, player: Player) : Rider(square, player) {
    companion object {
        init {
            PieceDrawer.addPiecePicture(TYPE_BISHOP, Player.WHITE, R.drawable.bishop_white)
            PieceDrawer.addPiecePicture(TYPE_BISHOP, Player.BLACK, R.drawable.bishop_black)
        }
    }

    override val type = TYPE_BISHOP

    override fun possibleMoves(board: Board, getMovesInDirection: (piece: Piece, board: Board, direction: Square, max_steps: Int) -> List<Square>): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                val direction = Square(i, j)
                moves.addAll(getMovesInDirection(this, board, direction, NO_MAX_STEPS))
            }
        }

        return moves
    }

    override fun copy(): Piece {
        return Bishop(square, player)
    }
}
