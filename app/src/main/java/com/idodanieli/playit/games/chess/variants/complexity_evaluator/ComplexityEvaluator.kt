package com.idodanieli.playit.games.chess.variants.complexity_evaluator

import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.variants.Game

class ComplexityEvaluator {
    companion object {
        // TODO: These numbers are made up - think of better ones
        private val PIECE_CATEGORY_COMPLEXITY_MULTIPLIERS = mapOf<PieceCategory, Float>(
            PieceCategory.CLASSIC to 1f,
            PieceCategory.FAIRY to 1.5f,
            PieceCategory.SPECIAL to 2f
        )
    }

    fun evaluate(game: Game): Float {
        var complexityScore = game.pieces().size.toFloat()

        for (piece in game.pieces()) {
            val category = PieceCategory.fromPiece(piece)
            val complexityMultiplier = PIECE_CATEGORY_COMPLEXITY_MULTIPLIERS[category] ?: throw Exception("No Multiplier for category $category $piece")

            complexityScore *= complexityMultiplier
        }

        return complexityScore
    }

    fun getPieceName(piece: Piece): String {
        piece::class.simpleName?.let { return it }

        return "Unknown"
    }
}