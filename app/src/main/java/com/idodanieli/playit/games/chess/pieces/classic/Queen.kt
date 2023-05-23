package com.idodanieli.playit.games.chess.pieces.classic

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.NO_MAX_STEPS
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.Rider

private val moveOffsets = arrayOf(1, -1, 0)
const val TYPE_QUEEN = "Q"

class Queen(square: Square, player: Player) : Rider(square, player) {
    override val type = TYPE_QUEEN

    override fun possibleMoves(board: Board, getMovesInDirection: (piece: Piece, board: Board, direction: Square, max_steps: Int) -> List<Square>): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                if (i == 0 && j == 0) { continue }

                val direction = Square(i, j)
                moves.addAll(getMovesInDirection(this, board, direction, NO_MAX_STEPS))
            }
        }

        return moves
    }
}