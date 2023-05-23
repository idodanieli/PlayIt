package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.*
import com.idodanieli.playit.games.chess.pieces.classic.Knight

private val MOVE_OFFSETS = arrayOf(2, 3, -2, -3)
const val TYPE_ZEBRA = "Z"

class Zebra(square: Square, player: Player) : Knight(square, player) {
    override val moveOffsets: Array<Int> = MOVE_OFFSETS
    override val type = TYPE_ZEBRA
}