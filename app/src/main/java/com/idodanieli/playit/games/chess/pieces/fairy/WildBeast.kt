package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.pieces.core.Compound


const val TYPE_WILDBEAST = "W"

class WildBeast(square: Square, player: Player) : Compound(square, player) {
    override val type = TYPE_WILDBEAST
    override val pieces = listOf(
        Knight(square, player),
        Camel(square, player)
    )
}