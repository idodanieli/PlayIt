package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*
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
        if (square.col != destination.col) {
            return false
        }

        // if not moved, pawn can move two squares
        if (!moved) {
            return abs(square.row - destination.row) <= MAX_START_MOVES
        }

        return square.row + direction == destination.row
    }

    override fun availableSquares(board: Board): List<Square> {
        val moves = arrayListOf<Square>()
        if (!moved) {
            moves.add(Square(square.col, square.row + direction * MAX_START_MOVES))
        }

        val defaultMove = Square(square.col, square.row + direction)
        if (board.isIn(defaultMove)) { moves.add(defaultMove) }

        val eatMove1 = Square(square.col - 1, square.row + direction)
        val eatMove2 = Square(square.col + 1, square.row + direction)
        for (move in listOf(eatMove1, eatMove2)) {
            // There is an enemy piece at the given square
            board.pieceAt(move)?.let {
                if (it.player != player) { moves.add(move) }
            }
        }

        return moves
    }

    override fun onMove() {
        super.onMove()

        this.moved = true
    }
}