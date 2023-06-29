package com.idodanieli.playit.games.chess.pieces.classic

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.ChessDrawer

private val KNIGHT_MOVE_OFFSETS = arrayOf(1, 2, -1, -2)
const val TYPE_KNIGHT = "N"

open class Knight(square: Square, player: Player) : BasePiece(square, player) {
    companion object {
        init {
            ChessDrawer.addPiecePicture(TYPE_KNIGHT, Player.WHITE, R.drawable.knight_white)
            ChessDrawer.addPiecePicture(TYPE_KNIGHT, Player.BLACK, R.drawable.knight_black)
        }
    }

    override val type = TYPE_KNIGHT
    open val moveOffsets = KNIGHT_MOVE_OFFSETS

    override fun availableSquares(board: Board): List<Square> {
        val moves = mutableSetOf<Square>()

        for (i in moveOffsets) {
            for (j in moveOffsets) {
                if (Math.abs(i) == Math.abs(j)) { continue }

                // TODO: Fix This -> Its redundant
                val move1 = Square(this.square.col + i, this.square.row + j)
                val move2 = Square(this.square.col + j, this.square.row + i)

                for (move in listOf(move1, move2)) {
                    if (board.isIn(move)) { moves.add(move) }
                }
            }
        }

        return moves.toList()
    }

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Knight(square, player)
    }
}