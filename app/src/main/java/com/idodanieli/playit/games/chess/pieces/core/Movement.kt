package com.idodanieli.playit.games.chess.pieces.core

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Square
import com.idodanieli.playit.games.chess.pieces.NO_MAX_STEPS
import com.idodanieli.playit.games.chess.pieces.Piece

enum class MovementType {
    REGULAR,
    LEAPER,
    RIDER,
    HOPPER
}

// allMovesInDirecton returns all the available moves in the given direction
// must be used ONLY for continuous piece like: Rook, Bishop, Queen, etc.
fun allMovesInDirecton(self: Piece, board: Board, direction: Square, max_steps: Int = NO_MAX_STEPS): List<Square> {
    val moves = arrayListOf<Square>()
    var move = self.square + direction
    var steps = 0

    while (board.isIn(move) && (max_steps == NO_MAX_STEPS || steps < max_steps)) {
        when(board.playerAt(move)) {
            // a piece as same as the bishop
            self.player -> {
                moves.add(move)
                break
            }
            // an enemy piece
            self.player.opposite() -> {
                moves.add(move)
                break
            }
            else -> {
                moves.add(move)
                move += direction
                steps += 1
            }
        }
    }

    return moves
}

// xrayMovesInDirection returns all the moves in the given direction like an xray
// must be used ONLY for continuous piece like: Rook, Bishop, Queen, etc.
fun xrayMovesInDirection(self: Piece, board: Board, direction: Square, max_steps: Int = NO_MAX_STEPS): List<Square> {
    val moves = arrayListOf<Square>()
    var move = self.square + direction
    var steps = 0

    while (board.isIn(move) && (max_steps == NO_MAX_STEPS || steps < max_steps)) {
        moves.add(move)
        move += direction
        steps += 1
    }

    return moves
}