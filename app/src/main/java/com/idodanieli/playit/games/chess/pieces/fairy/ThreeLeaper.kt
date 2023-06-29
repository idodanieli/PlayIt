package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.DIRECTIONS
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.Hopper

class ThreeLeaper(square: Square, player: Player) : Hopper(square, player) {
    override val directions: List<Square> = DIRECTIONS.map { it.value }.
        filter { it.isVerticalDirection() || it.isHorizontalDirection()}
    override val hopSize: Int = 3

    // TODO: This is not a letter :(
    override val type = "Three Leaper"

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return ThreeLeaper(square, player)
    }
}