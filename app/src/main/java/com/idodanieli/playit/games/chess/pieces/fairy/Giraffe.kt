package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.Knight

private val MOVE_OFFSETS = arrayOf(1, 4, -1, -4)
const val TYPE_GIRAFFE = "G"

class Giraffe(square: Square, player: Player) : Knight(square, player) {
    override val moveOffsets: Array<Int> = MOVE_OFFSETS
    override val type = TYPE_GIRAFFE
}