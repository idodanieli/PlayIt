package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.NOT_DIAGONAL_DIRECTIONS
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.StaticHopper

class ThreeLeaper(square: Square, player: Player) : StaticHopper(square, player) {
    override val directions: List<Square> = NOT_DIAGONAL_DIRECTIONS.map { it.value }
    override val hopSize: Int = 3

    // TODO: This is not a letter :(
    override val type = "Three Leaper"

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return ThreeLeaper(square, player)
    }
}