package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Player
import com.idodanieli.playit.games.chess.Square

// A hopper is a piece that moves by jumping over another piece (called a hurdle).
// The hurdle can be any piece of any color. Unless it can jump over a piece, a hopper cannot move.
// Note that hoppers generally capture by taking the piece on the destination square, not by taking the hurdle (as is the case in checkers).
// The exceptions are locusts which are pieces that capture by hopping over its victim. They are sometimes considered a type of hopper.
abstract class Hopper(square: Square, player: Player) : BasePiece(square, player) {
    override val movementType = MovementType.HOPPER

    // The direction the Hopper hops in
    abstract val direction: Square

    // The size of the hop the hopper makes
    abstract val hop: Int

    override fun possibleMoves(board: Board): List<Square> {
        val moves = arrayListOf<Square>()
                
        moves.add(Square(square.col + direction.col * hop, square.row + direction.row * hop))
        moves.add(Square(square.col - direction.col * hop, square.row + direction.row * hop))
        moves.add(Square(square.col + direction.col * hop, square.row - direction.row * hop))
        moves.add(Square(square.col - direction.col * hop, square.row - direction.row * hop))

        return moves.filter { board.isIn(it) }
    }
}