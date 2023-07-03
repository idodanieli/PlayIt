package com.idodanieli.playit.games.chess.variants

import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.TYPE_KING

open class CaptureOrCheckmate(name: String, startingPieces: Set<Piece>, size: Int): ClassicGame(name, startingPieces, size) {
    companion object {
        const val TYPE = "capture_or_checkmate"
    }

    override fun isOver(): Boolean {
        val king = board.getPiece(TYPE_KING, currentPlayer)

        return king == null || super.isOver()
    }
}