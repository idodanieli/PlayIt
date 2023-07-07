package com.idodanieli.playit.games.chess.variants

import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.TYPE_KING

open class CaptureOrCheckmate(name: String, startingPieces: Set<Piece>, colCount: Int, rowCount: Int): ClassicGame(name, startingPieces, colCount, rowCount) {
    companion object {
        const val TYPE = "capture_or_checkmate"
    }

    override fun isOver(): Boolean {
        val king = board.getPiece(TYPE_KING, currentPlayer)

        return king == null || super.isOver()
    }
}