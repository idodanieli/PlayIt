package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.pieces.core.Compound


const val TYPE_CENTAUR = "S"

class Centaur(square: Square, player: Player) : Compound(square, player) {
    override val type = TYPE_CENTAUR
    override val pieces = listOf(
        Knight(square, player),
        Man(square, player)
    )

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Centaur(square, player)
    }
}