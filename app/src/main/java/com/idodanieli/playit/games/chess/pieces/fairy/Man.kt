package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece

open class Man(square: Square, player: Player) : BasePiece(square, player) {
    companion object {
        const val TYPE_MAN = "Y"
    }

    override val type = TYPE_MAN

    override fun availableSquares(board: Board): List<Square> {
        return board.neighborSquares(this)
    }

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Man(square, player)
    }
}