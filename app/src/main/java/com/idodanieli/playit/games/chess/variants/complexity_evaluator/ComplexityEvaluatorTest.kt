package com.idodanieli.playit.games.chess.variants.complexity_evaluator

import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.abilities.Bomber
import com.idodanieli.playit.games.chess.pieces.classic.Queen
import com.idodanieli.playit.games.chess.pieces.fairy.Archbishop
import org.junit.Test

class ComplexityEvaluatorTest {
    private val evaluator = ComplexityEvaluator()

    private val square = Square(0, 0)
    private val queen = Queen(square, Player.WHITE)
    private val archbishop = Archbishop(square, Player.WHITE)

    @Test
    fun testGetPieceName() {
        val result = evaluator.getPieceName(queen)

        assert(result == "Queen") {
            "name of $queen should have been Queen instead of: $result"
        }
    }

    @Test
    fun testGetPieceCategory() {
        assert(PieceCategory.fromPiece(queen) == PieceCategory.CLASSIC)
        assert(PieceCategory.fromPiece(archbishop) == PieceCategory.FAIRY)
    }
}