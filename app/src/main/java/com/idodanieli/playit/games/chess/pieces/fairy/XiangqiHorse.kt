package com.idodanieli.playit.games.chess.pieces.fairy
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.drawers.PieceDrawer

private val XIANGQI_HORSE_MOVE_OFFSETS = arrayOf(1, 2, -1, -2)
const val TYPE_XIANGQI_HORSE = "O"

// TODO: FIX THIS CLASS, THE MOVEMENT IS WRONG
open class XiangqiHorse(square: Square, player: Player) : BasePiece(square, player) {
    companion object {
        init {
            PieceDrawer.addPiecePicture(TYPE_XIANGQI_HORSE, Player.WHITE, R.drawable.xiangqi_horse_white)
            PieceDrawer.addPiecePicture(TYPE_XIANGQI_HORSE, Player.BLACK, R.drawable.xiangqi_horse_black)
        }
    }

    override val type = TYPE_XIANGQI_HORSE
    open val moveOffsets = XIANGQI_HORSE_MOVE_OFFSETS

    override fun availableSquares(board: Board): List<Square> {
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

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return XiangqiHorse(square, player)
    }
}