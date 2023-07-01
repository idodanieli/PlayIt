package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.pieces.abilities.Bomber
import com.idodanieli.playit.games.chess.pieces.core.PieceWrapper
import kotlin.reflect.full.allSuperclasses

private val EXPLANATIONS = mapOf (
    Bomber::class.simpleName!! to "The Bomber can explode, removing itself and each neighboring piece from the board"
)

class PieceGuide {
    companion object {
        fun explain(piece: Piece): String {
            val name = getPieceName(piece)

            var explanation = ""


            getPieceParent(piece)?.let { parent ->
                explanation += "The $name is a $parent\n"

                if (parent in EXPLANATIONS) {
                    explanation += EXPLANATIONS[parent]
                }
            }

            return explanation
        }

        fun getPieceName(piece: Piece): String {
            piece::class.simpleName?.let { return it }

            return "Unknown"
        }

        fun getPieceParent(piece: Piece): String? {
            val parents = getPieceParents(piece)

            if (parents.isNotEmpty()) {
                return  parents[0]
            }

            return null
        }

        private fun getPieceParents(piece: Piece): List<String> {
            val allParents =  piece::class.allSuperclasses.map { it.simpleName.toString() }

            return allParents.takeWhile { it !in listOf(Piece::class.simpleName, BasePiece::class.simpleName, PieceWrapper::class.simpleName) }
        }
    }
}