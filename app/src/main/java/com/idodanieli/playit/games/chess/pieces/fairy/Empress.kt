package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.pieces.core.Compound
import com.idodanieli.playit.games.chess.pieces.classic.Rook


const val TYPE_EMPRESS = "M"

class Empress(square: Square, player: Player) : Compound(square, player) {
    override val type = TYPE_EMPRESS
    override val pieces = listOf(
        Knight(square, player),
        Rook(square, player)
    )
}