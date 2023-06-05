package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.classic.Knight
import com.idodanieli.playit.games.chess.pieces.core.MovementType


const val TYPE_CENTAUR = "S"

class Centaur(square: Square, player: Player) : BasePiece(square, player) {
    override val type = TYPE_CENTAUR
    override val movementType = MovementType.LEAPER

    override fun possibleMoves(board: Board): List<Move> {
        return Knight(square, player).possibleMoves(board) +
                board.neighborSquares(this).map { Move(square, it, player) }
    }
}