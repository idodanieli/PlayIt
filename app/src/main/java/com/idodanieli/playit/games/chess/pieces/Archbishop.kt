package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*


const val TYPE_ARCHBISHOP = "@"

class Archbishop(square: Square, player: Player) : Compound(square, player) {
    override val type = TYPE_ARCHBISHOP
    override val movementType = MovementType.RIDER
    override val pieces = listOf(
        Knight(square, player),
        Bishop(square, player)
    )
}