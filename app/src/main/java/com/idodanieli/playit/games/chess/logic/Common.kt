package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.pieces.Piece

fun deepCopyPieces(pieces: Set<Piece>): MutableSet<Piece> {
    val copiedPieces = mutableSetOf<Piece>()
    for (piece in pieces) {
        val constructor = piece::class.java.constructors.first()
        val copiedPiece = constructor.newInstance(piece.square.copy(), piece.player) as Piece
        copiedPieces.add(copiedPiece)
    }

    return copiedPieces
}

// getPieceByType returns the first piece it finds that is of the given type
fun getPieceByType(pieces: MutableMap<Piece, Boolean>, type: String): Piece? {
    for (piece in pieces.keys) {
        if (piece.type == type) {
            return piece
        }
    }

    return null
}