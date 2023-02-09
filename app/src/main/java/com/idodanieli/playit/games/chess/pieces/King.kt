package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

open class King(square: Square, player: Player) : BasePiece(square, player) {
    override val type = Type.KING
    override val movementType = MovementType.LEAPER

    override fun possibleMoves(board: Board): List<Square> {
        return board.getAvailableNeighborSquares(this).
        filter { !board.isThreatened(it, player.opposite()) }
    }
}