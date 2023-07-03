package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.pieces.Piece
import kotlin.random.Random

fun deepCopyPieces(pieces: Set<Piece>): MutableSet<Piece> {
    val copiedPieces = mutableSetOf<Piece>()
    for (piece in pieces) {
        copiedPieces.add(piece.copy())
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

fun <T> getRandomItem(collection: Iterable<T>): T {
    val random = Random(System.currentTimeMillis())
    val idx = random.nextInt(collection.count())
    return collection.elementAt(idx)
}