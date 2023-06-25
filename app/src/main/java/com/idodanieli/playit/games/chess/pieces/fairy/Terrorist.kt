package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.abilities.Bomber

class Terrorist(square: Square, player: Player) : Bomber(BasePiece(square, player)) {
    companion object {
        const val TYPE = "T"
    }

    override val type = TYPE

    override fun availableMoves(board: Board): List<Move> {
        return (availableSquares(board) + capturableSquares(board)).map { square -> Move(this.square, square) }
    }

    override fun availableSquares(board: Board): List<Square> {
        val squares = arrayListOf<Square>()

        squares += this.square + Square(1, 0)
        squares += this.square + Square(-1, 0)
        squares += this.square + Square(0, 1)
        squares += this.square + Square(0, -1)

        return squares.filter{it.inBorder(board.size)}
    }
}