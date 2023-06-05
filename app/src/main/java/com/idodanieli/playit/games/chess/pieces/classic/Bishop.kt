package com.idodanieli.playit.games.chess.pieces.classic

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.core.MovementType
import com.idodanieli.playit.games.chess.pieces.NO_MAX_STEPS
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.Rider

private val moveOffsets = arrayOf(1, -1)
const val TYPE_BISHOP = "B"

class Bishop(square: Square, player: Player) : Rider(square, player) {
    override val type = TYPE_BISHOP
    override val movementType = MovementType.RIDER

    override fun possibleMoves(board: Board, getMovesInDirection: (piece: Piece, board: Board, direction: Square, max_steps: Int) -> List<Move>): List<Move> {
        val moves = arrayListOf<Move>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                val direction = Square(i, j)
                moves.addAll(getMovesInDirection(this, board, direction, NO_MAX_STEPS))
            }
        }

        return moves
    }
}
