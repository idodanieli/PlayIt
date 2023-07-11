package com.idodanieli.playit.games.chess.variants.complexity_evaluator

import com.idodanieli.playit.games.chess.pieces.Piece

enum class PieceCategory {
    UNKNOWN,
    CLASSIC,
    FAIRY,
    SPECIAL;

    companion object {
        fun fromPackage(name: String): PieceCategory {
            if (name.contains("classic")) return CLASSIC
            if (name.contains("fairy")) return FAIRY
            if (name.contains("abilities")) return SPECIAL

            return UNKNOWN
        }

        fun fromPiece(piece: Piece): PieceCategory {
            val packageName = getPiecePackageName(piece) ?: return PieceCategory.UNKNOWN

            return fromPackage(packageName)
        }
    }
}

fun getPiecePackageName(piece: Piece): String? {
    return piece.javaClass.`package`?.name
}