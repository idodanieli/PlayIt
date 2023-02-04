package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private val moveOffsets = arrayOf(-1, 0, 1)

class King(square: Square, player: Player) : BasePiece(square, player) {
    override val type = Type.KING

    override fun availableSquares(board: Board): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                if (i == 0 && j == 0) { continue }

                val move = Square(square.col + i, square.row + j)

                if (board.isIn(move) && board.playerAt(move) != player && !isThreatened(move, board)) {
                    moves.add(move)
                }
            }
        }

        return moves
    }

    // isThreatened returns true if the square is threatened by another piece
    private fun isThreatened(square: Square, board: Board): Boolean {
        val enemyPieces = board.pieces.filter { it.player == player.opposite() }
        for (piece in enemyPieces) {
            if (piece.type == Type.KING) { // To avoid recursion
                if (piece.square.isNear(square)) return true
                continue
            }

            if (square in piece.availableSquares(board)) {
                return true
            }
        }

        return false
    }
}