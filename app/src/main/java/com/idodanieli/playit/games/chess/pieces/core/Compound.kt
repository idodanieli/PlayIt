package com.idodanieli.playit.games.chess.pieces.core

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece

abstract class Compound(square: Square, player: Player) : BasePiece(square, player) {

    abstract val pieces: List<Piece>

    override fun possibleMoves(board: Board): List<Move> {
        val moves = arrayListOf<Move>()

        for (piece in pieces) { moves += piece.possibleMoves(board) }

        return moves
    }

    override fun onMove() {
        super.onMove()

        for (piece in pieces) { piece.square = square }
    }
}