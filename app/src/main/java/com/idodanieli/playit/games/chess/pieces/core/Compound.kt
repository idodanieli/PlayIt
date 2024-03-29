package com.idodanieli.playit.games.chess.pieces.core

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece

abstract class Compound(square: Square, player: Player) : BasePiece(square, player) {

    abstract val pieces: List<Piece>

    override fun availableSquares(board: Board): List<Square> {
        val moves = mutableSetOf<Square>()

        for (piece in pieces) { moves += piece.availableSquares(board) }

        return moves.toList()
    }

    override fun onMove() {
        super.onMove()

        for (piece in pieces) { piece.square = square }
    }
}