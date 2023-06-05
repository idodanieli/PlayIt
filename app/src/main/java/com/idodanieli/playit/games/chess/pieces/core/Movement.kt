package com.idodanieli.playit.games.chess.pieces.core

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.NO_MAX_STEPS
import com.idodanieli.playit.games.chess.pieces.Piece

enum class MovementType {
    REGULAR,
    LEAPER,
    RIDER,
    HOPPER
}

// allMovesInDirection returns all the available moves in the given direction
// must be used ONLY for continuous piece like: Rook, Bishop, Queen, etc.
fun allMovesInDirection(self: Piece, board: Board, direction: Square, max_steps: Int = NO_MAX_STEPS): List<Move> {
    val destinations = arrayListOf<Square>()
    var dest = self.square + direction
    var steps = 0

    while (board.isIn(dest) && (max_steps == NO_MAX_STEPS || steps < max_steps)) {
        when(board.playerAt(dest)) {
            // a piece as same as the bishop
            self.player -> {
                destinations.add(dest)
                break
            }
            // an enemy piece
            self.player.opposite() -> {
                destinations.add(dest)
                break
            }
            else -> {
                destinations.add(dest)
                dest += direction
                steps += 1
            }
        }
    }

    return destinations.map { Move(self.square, it, self.player) }
}

// xrayMovesInDirection returns all the moves in the given direction like an xray
// must be used ONLY for continuous piece like: Rook, Bishop, Queen, etc.
fun xrayMovesInDirection(self: Piece, board: Board, direction: Square, max_steps: Int = NO_MAX_STEPS): List<Move> {
    val destinations = arrayListOf<Square>()
    var dest = self.square + direction
    var steps = 0

    while (board.isIn(dest) && (max_steps == NO_MAX_STEPS || steps < max_steps)) {
        destinations.add(dest)
        dest += direction
        steps += 1
    }

    return destinations.map { Move(self.square, it, self.player) }
}
