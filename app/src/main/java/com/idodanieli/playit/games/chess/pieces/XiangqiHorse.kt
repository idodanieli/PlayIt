package com.idodanieli.playit.games.chess.pieces

import android.util.Log
import com.idodanieli.playit.games.chess.*

private val XIANGQI_HORSE_MOVE_OFFSETS = arrayOf(1, 2, -1, -2)
const val TYPE_XIANGQI_HORSE = "U"

// TODO: FIX THIS CLASS, THE MOVEMENT IS WRONG
open class XiangqiHorse(square: Square, player: Player) : BasePiece(square, player) {
    override val type = TYPE_XIANGQI_HORSE
    override val movementType = MovementType.REGULAR
    open val moveOffsets = XIANGQI_HORSE_MOVE_OFFSETS

    override fun possibleMoves(board: Board): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                if (Math.abs(i) == Math.abs(j)) { continue }
                
                val move = Square(i, j)
                val destination = square + move

                if(board.isIn(destination) && !isMoveBlockedByOtherPiece(move, board)) {
                    moves.add(destination)
                }
            }
        }

        return moves
    }
    
    private fun isMoveBlockedByOtherPiece(move: Square, board: Board): Boolean {
        var isBlocked = false
        for (square in square.squaresPassedInMove(move, moveByRowFirst = true, excludeDestination = true)) {
            board.pieceAt(square)?.let {
                isBlocked = true
            }
        }

        if (!isBlocked) { return false }
        for (square in square.squaresPassedInMove(move, moveByRowFirst = false, excludeDestination = true)) {
            board.pieceAt(square)?.let {
                return true
            }
        }
        
        return false
    }
}