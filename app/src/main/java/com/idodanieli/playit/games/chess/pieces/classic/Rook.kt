package com.idodanieli.playit.games.chess.pieces.classic

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.NO_MAX_STEPS
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.Rider
import com.idodanieli.playit.games.chess.ui.ChessDrawer
import kotlin.math.max

private val moveOffsets = arrayOf(1, -1, 0)
const val TYPE_ROOK = "R"

class Rook(square: Square, player: Player) : Rider(square, player) {
    companion object {
        init {
            ChessDrawer.addPiecePicture(TYPE_ROOK, Player.WHITE, R.drawable.rook_white)
            ChessDrawer.addPiecePicture(TYPE_ROOK, Player.BLACK, R.drawable.rook_black)
        }
    }

    override val type = TYPE_ROOK

    private var maxSteps = NO_MAX_STEPS

    override fun possibleMoves(board: Board, getMovesInDirection: (piece: Piece, board: Board, direction: Square, max_steps: Int) -> List<Square>): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                if (i == 0 && j == 0 || Math.abs(i) == Math.abs(j)) { continue }

                val direction = Square(i, j)
                moves.addAll(getMovesInDirection(this, board, direction, maxSteps))
            }
        }

        return moves
    }

    fun setMaxSteps(maxSteps: Int) {
        this.maxSteps = maxSteps
    }

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Rook(square, player)
    }
}