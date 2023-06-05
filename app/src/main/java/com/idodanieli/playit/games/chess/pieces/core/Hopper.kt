package com.idodanieli.playit.games.chess.pieces.core

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece

// A hopper is a piece that moves by jumping over another piece (called a hurdle).
// The hurdle can be any piece of any color. Unless it can jump over a piece, a hopper cannot move.
// Note that hoppers generally capture by taking the piece on the destination square, not by taking the hurdle (as is the case in checkers).
// The exceptions are locusts which are pieces that capture by hopping over its victim. They are sometimes considered a type of hopper.
abstract class Hopper(square: Square, player: Player) : BasePiece(square, player) {
    override val movementType = MovementType.HOPPER

    // The direction the Hopper hops in
    abstract val directions: List<Square>

    // The size of the hop the hopper makes
    abstract val hop: Int

    override fun possibleMoves(board: Board): List<Move> {
        val destinations = arrayListOf<Square>()

        for (direction in directions) {
            destinations.add(Square(square.col + direction.col * hop, square.row + direction.row * hop))
        }

        return destinations.filter { board.isIn(it) }.map { Move(square, it, player) }
    }

    // isAHopOverOtherPiece checks if the given move hops over another move
    fun isAHopOverAPiece(move: Move, board: Board): Boolean {
        return square.squaresBetween(move.dest, excludeDestination = true).any { board.pieceAt(it) != null }
    }
}