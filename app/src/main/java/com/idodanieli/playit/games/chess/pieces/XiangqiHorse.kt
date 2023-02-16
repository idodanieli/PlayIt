package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private val XIANGQI_HORSE_MOVE_OFFSETS = arrayOf(1, 2, -1, -2)
const val TYPE_XIANGQI_HORSE = "U"

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
                if(!isMoveBlockedByOtherPiece(move, board)) { moves.add(move) }
            }
        }

        return moves
    }
    
    private fun isMoveBlockedByOtherPiece(move: Square, board: Board): Boolean {
        for (square in square.squaresPassedInMove(move, excludeDestination = true)) {
            board.pieceAt(square)?.let {
                return true
            }
        }
        
        return false
    }
}