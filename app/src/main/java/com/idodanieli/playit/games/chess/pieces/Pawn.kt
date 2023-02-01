package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Square
import java.lang.Math.abs

const val WHITE_DIRECTION = 1
const val BLACK_DIRECTION = -1
const val MAX_START_MOVES = 2

class Pawn(square: Square, player: Player, type: Type) : BasePiece(square, player, type) {
    var direction = WHITE_DIRECTION
    var moved = false // specifies whether the pawn has been moved from its starting square

    init {
        if (player == Player.BLACK) {
            direction = BLACK_DIRECTION
        }
    }

    override fun canMove(destination: Square, board: Board): Boolean {
        // Pawn can only move up in a given column
        if (this.square.col != destination.col) {
            return false
        }

        // if not moved, pawn can move two squares
        if (!this.moved) {
            return abs(this.square.row - destination.row) <= MAX_START_MOVES
        }

        return this.square.row + direction == destination.row
    }

    override fun onMove() {
        super.onMove()

        this.moved = true
    }
}