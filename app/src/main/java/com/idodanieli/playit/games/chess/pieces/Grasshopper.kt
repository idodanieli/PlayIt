package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Player
import com.idodanieli.playit.games.chess.Square

// The grasshopper is a fairy chess piece that moves along ranks, files, and diagonals (as a queen)
// but only by hopping over another piece. The piece to be hopped may be any distance away,
// but the grasshopper must land on the square immediately behind it in the same direction.
// If there is no piece to hop over, it cannot move.
// If the square beyond a piece is occupied by a piece of the opposite color, the grasshopper can capture that piece.
// The grasshopper may jump over pieces of either color; the piece being jumped over is unaffected.
class Grasshopper(square: Square, player: Player) : Hopper(square, player) {
    override val directions: List<Square> = listOf(
        Square(1, 1),
        Square(1, -1),
        Square(1, 0),
        Square(-1, -1),
        Square(-1, 1),
        Square(-1, 0),
        Square(0, 1),
        Square(0, -1),
    )
    override val hop: Int = 3
    override val type: Type = Type.GRASSHOPPER

    override fun possibleMoves(board: Board): List<Square> {
        val moves = super.possibleMoves(board)

        return moves.filter { isAHopOverAPiece(it, board) }
    }
}