package com.idodanieli.playit.games.chess.pieces.classic

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.core.MovementType

const val TYPE_KING = "K"

open class King(square: Square, player: Player) : BasePiece(square, player) {
    override val type = TYPE_KING
    override val movementType = MovementType.LEAPER

    override fun validMoves(board: Board, ignoreCheck: Boolean, ignoreSamePlayer: Boolean): List<Square> {
        return possibleMoves(board)
    }

    override fun possibleMoves(board: Board): List<Square> {
        return board.getAvailableNeighborSquares(this)
            .filter { !board.isThreatened(it, player.opposite()) }
    }
}