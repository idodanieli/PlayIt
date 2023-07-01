package com.idodanieli.playit.games.chess.ui

import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.variants.Game

fun getHeroTouchData(game: Game, touchedSquare: Square): TouchData? {
    val touchData = getTouchData(game, touchedSquare) ?: return null

    // If the touched piece is not of the current player - display nothing
    if (touchData.piece.player != game.currentPlayer) {
        return null
    }

    return touchData
}

fun getTouchData(game: Game, touchedSquare: Square): TouchData? {
    val touchedPiece = game.board.pieceAt(touchedSquare) ?: return null

    return TouchData(touchedSquare, touchedPiece, getAvailableMoves(game, touchedPiece))
}

private fun getAvailableMoves(game: Game, piece: Piece): Map<Move, Move> {
    return game.getLegalMovesForPiece(piece).associateWith { it }
}