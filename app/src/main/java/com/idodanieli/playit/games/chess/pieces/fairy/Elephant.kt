package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.DIRECTIONS
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.King
import com.idodanieli.playit.games.chess.pieces.core.Hopper

const val TYPE_ELEPHANT = "E"

// Also known as Alfil
// Moves like this:
// . . . . . . . .
// . . . . . . . .
// . X . . . X . .
// . . . . . . . .
// . . . E . . . .
// . . . . . . . .
// . X . . . X . .
// . . . . . . . .
class Elephant(square: Square, player: Player) : Hopper(square, player) {
    override val directions: List<Square> = DIRECTIONS.map { it.value }.filter { it.isDiagonalDirection() }
    override val hopSize: Int = 2
    override val type = TYPE_ELEPHANT

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Elephant(square, player)
    }
}