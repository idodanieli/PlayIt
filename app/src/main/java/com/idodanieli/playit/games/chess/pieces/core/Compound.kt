package com.idodanieli.playit.games.chess.pieces.core

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece

abstract class Compound(square: Square, player: Player) : BasePiece(square, player) {

    abstract val pieces: List<Piece>

    override fun possibleMoves(board: Board): List<Square> {
        val moves = arrayListOf<Square>()

        for (piece in pieces) { moves += piece.possibleMoves(board) }

        return moves
    }

    override fun onMove() {
        super.onMove()

        for (piece in pieces) { piece.square = square }
    }
}