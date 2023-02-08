package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private val MOVE_OFFSETS = arrayOf(2, 3, -2, -3)

class Zebra(square: Square, player: Player) : Knight(square, player) {
    override val moveOffsets: Array<Int> = MOVE_OFFSETS
    override val type = Type.ZEBRA
}