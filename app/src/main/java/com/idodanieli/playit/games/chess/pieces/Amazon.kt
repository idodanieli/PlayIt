package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*


const val TYPE_AMAZON = "A"

class Amazon(square: Square, player: Player) : Compound(square, player) {
    override val type = TYPE_AMAZON
    override val movementType = MovementType.LEAPER
    override val pieces = listOf(
        Knight(square, player),
        Queen(square, player)
    )
}