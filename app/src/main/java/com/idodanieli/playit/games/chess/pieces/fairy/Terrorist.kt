package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.abilities.Bomber
import com.idodanieli.playit.games.chess.pieces.classic.Rook

class Terrorist(square: Square, player: Player) : Bomber(Rook(square, player)) {
    companion object {
        const val TYPE = "T"

        private const val MAX_STEPS = 1
    }

    init {
        val child = this.getChild() as Rook
        child.setMaxSteps(MAX_STEPS)
    }

    override val type = TYPE


    override fun capturableSquares(board: Board): List<Square> {
        return this.explosionThreatenedSquares(board)
    }

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Terrorist(square, player)
    }
}