package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.core.MovementType

open class BasePiece(override var square: Square, override var player: Player): Piece {
    override val type = ""
    override val movementType = MovementType.REGULAR
    override var moved = false

    override fun availableMoves(board: Board): List<Move> {
        return availableSquares(board).map { dest -> Move(square, dest, player) }
    }

    override fun availableSquares(board: Board): List<Square> {
        // TODO: Raise NotImplementedException!
        // To be overridden by child classes
        return emptyList()
    }

    override fun xrayAvailableMoves(board: Board): List<Square> {
        return emptyList()
    }

    override fun capturableSquares(board: Board): List<Square> {
        return availableSquares(board)
    }

    override fun onMove() {
        this.moved = true
    }

    override fun onCaptured(capturedPiece: Piece) {
        return
    }

    override fun toString(): String {
        val type = if (player.isWhite()) type else type.lowercase()
        return "$player $type at (${square.col}, ${square.row})"
    }
}