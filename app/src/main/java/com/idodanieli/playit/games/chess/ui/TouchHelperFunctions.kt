package com.idodanieli.playit.games.chess.ui

import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.variants.Game

fun createTouchData(game: Game, touchedSquare: Square): TouchData? {
    val touchedPiece = getTouchedPiece(game, touchedSquare)
    if (touchedPiece != null) {
        return TouchData(touchedSquare, touchedPiece, getAvailableMoves(game, touchedPiece))
    }

    return null
}

private fun getAvailableMoves(game: Game, piece: Piece): Map<Move, Move> {
    return game.getLegalMovesForPiece(piece).associateWith { it }
}

private fun getTouchedPiece(game: Game, touchedSquare: Square): Piece? {
    val touchedPiece = game.board.pieceAt(touchedSquare)

    // If the touched piece is not of the current player - display nothing
    if (touchedPiece == null || game.currentPlayer != touchedPiece.player) {
        return null
    }

    return touchedPiece
}