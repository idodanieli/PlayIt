package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Player
import com.idodanieli.playit.games.chess.Square

abstract class Compound(square: Square, player: Player) : BasePiece(square, player) {

    abstract val pieces: List<Piece>

    override fun possibleMoves(board: Board): List<Square> {
        val moves = arrayListOf<Square>()

        for (piece in pieces) { moves += piece.possibleMoves(board) }

        return moves
    }

    override fun onMove() {
        for (piece in pieces) { piece.square = square }
    }
}