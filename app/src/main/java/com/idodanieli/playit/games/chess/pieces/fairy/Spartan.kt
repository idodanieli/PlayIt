package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.core.RestrictedPiece
import com.idodanieli.playit.games.chess.ui.ChessDrawer

class Spartan(square: Square, player: Player): RestrictedPiece(square, player) {
    companion object {
        const val TYPE = "I"

        init {
            ChessDrawer.addPiecePicture(TYPE, Player.WHITE, R.drawable.spartan_white)
            ChessDrawer.addPiecePicture(TYPE, Player.BLACK, R.drawable.spartan_black)
        }
    }

    override val type = TYPE

    override fun availableSquares(board: Board): List<Square> {
        val squares = listOf(
            this.squareInFront(),
            this.squareBehind()
        )

        return squares.filter { board.isIn(it) }
    }

    override fun capturableSquares(board: Board): List<Square> {
        val squares = listOf(
            this.squareInFront()
        )

        return squares.filter { board.isIn(it) }
    }

    private fun squareInFront(): Square {
        if (player.isWhite()) {
            return square + Square(0, 1)
        }

        return square + Square(0, -1)
    }

    private fun squareBehind(): Square {
        if (player.isBlack()) {
            return square + Square(0, 1)
        }

        return square + Square(0, -1)
    }
}