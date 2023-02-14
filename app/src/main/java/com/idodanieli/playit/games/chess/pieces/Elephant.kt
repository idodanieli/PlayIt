package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Player
import com.idodanieli.playit.games.chess.Square

// Despite sounding like a powerful piece, the chaturanga elephant can only move diagonally two squares.
// Like knights, elephants can jump over other pieces.
// The leaping powers of the elephant might seem like a triumph.
// Nevertheless, they can control just four squares at a time, and their regular movement (as opposed to the knight's irregular jump)
// restricts their reach—each elephant can only ever access eight specific squares of the board.
class Elephant(square: Square, player: Player) : Hopper(square, player) {
    override val directions: List<Square> = listOf(
        Square(1, 1),
        Square(1, -1),
        Square(-1, -1),
        Square(-1, 1),
    )
    override val hop: Int = 2
    override val type: Type = Type.ELEPHANT
}