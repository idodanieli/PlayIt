package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.core.MovementType

open class BasePiece(override var square: Square, override var player: Player): Piece {
    override val type = ""
    override val movementType = MovementType.REGULAR
    override var moved = false

    // validMoves returns a list of the squares the piece can move to
    override fun validMoves(board: Board): List<Square> {
        var moves = possibleMoves(board)
        return moves
    }

    // possibleMoves returns all the squares a piece can move to, without taking general logic
    // into consideration like pinning, etc.
    override fun possibleMoves(board: Board): List<Square> {
        // To be overridden by child classes
        return emptyList()
    }

    override fun xrayPossibleMove(board: Board): List<Square> {
        return emptyList()
    }

    override fun captureMoves(board: Board): List<Square> {
        return possibleMoves(board)
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