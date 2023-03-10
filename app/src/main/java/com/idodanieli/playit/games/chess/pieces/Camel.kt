package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private val MOVE_OFFSETS = arrayOf(1, 3, -1, -3)
const val TYPE_CAMEL = "c"

// The camel or long knight is a fairy chess piece with an elongated knight move.
// It can jump three squares horizontally and one square vertically or three squares vertically and one square horizontally,
// regardless of intervening pieces. Therefore, it is a (1,3)-leaper.
class Camel(square: Square, player: Player) : Knight(square, player) {
    override val moveOffsets: Array<Int> = MOVE_OFFSETS
    override val type = TYPE_CAMEL
}