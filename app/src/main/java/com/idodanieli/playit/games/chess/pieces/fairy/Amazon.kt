package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.King
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.pieces.core.Compound
import com.idodanieli.playit.games.chess.pieces.classic.Queen


const val TYPE_AMAZON = "A"

class Amazon(square: Square, player: Player) : Compound(square, player) {
    override val type = TYPE_AMAZON
    override val pieces = listOf(
        Knight(square, player),
        Queen(square, player)
    )

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Amazon(square, player)
    }
}