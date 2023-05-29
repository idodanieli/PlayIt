package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.pieces.Piece

fun deepCopyPieces(pieces: MutableSet<Piece>): MutableSet<Piece> {
    val copiedPieces = mutableSetOf<Piece>()
    for (piece in pieces) {
        val constructor = piece::class.java.constructors.first()
        val copiedPiece = constructor.newInstance(piece.square.copy(), piece.player) as Piece
        copiedPieces.add(copiedPiece)
    }

    return copiedPieces
}