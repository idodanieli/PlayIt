package com.idodanieli.playit.games.chess.pieces.core

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece

// A hopper is a piece that moves by jumping over another piece (called a hurdle).
// The hurdle can be any piece of any color. Unless it can jump over a piece, a hopper cannot move.
// Note that hoppers generally capture by taking the piece on the destination square, not by taking the hurdle (as is the case in checkers).
// The exceptions are locusts which are pieces that capture by hopping over its victim. They are sometimes considered a type of hopper.
abstract class Hopper(square: Square, player: Player) : BasePiece(square, player) {

    // The direction the Hopper hops in
    abstract val directions: List<Square>

    // The size of the hop the hopper makes
    abstract val hopSize: Int

    override fun availableSquares(board: Board): List<Square> {
        val destinations = arrayListOf<Square>()

        for (direction in directions) {
            val destination = Square(square.col + direction.col * hopSize, square.row + direction.row * hopSize)

            destinations.add(destination)
        }

        return destinations.filter { board.isIn(it) }
    }

    // isAHopOverOtherPiece checks if the given move hops over another move
    fun isAHopOverAPiece(move: Square, board: Board): Boolean {
        return square.squaresBetween(move, excludeDestination = true).any { board.pieceAt(it) != null }
    }
}