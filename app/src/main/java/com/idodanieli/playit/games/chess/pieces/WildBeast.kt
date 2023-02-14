package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*


const val TYPE_WILDBEAST = "W"

class WildBeast(square: Square, player: Player) : Compound(square, player) {
    override val type = TYPE_WILDBEAST
    override val movementType = MovementType.LEAPER
    override val pieces = listOf(
        Knight(square, player),
        Camel(square, player)
    )
}