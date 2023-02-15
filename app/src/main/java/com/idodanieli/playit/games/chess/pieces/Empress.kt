package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*


const val TYPE_EMPRESS = "e"

class Empress(square: Square, player: Player) : Compound(square, player) {
    override val type = TYPE_EMPRESS
    override val movementType = MovementType.RIDER
    override val pieces = listOf(
        Knight(square, player),
        Rook(square, player)
    )
}