package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.King
import com.idodanieli.playit.games.chess.pieces.classic.Knight


const val TYPE_CENTAUR = "S"

class Centaur(square: Square, player: Player) : BasePiece(square, player) {
    override val type = TYPE_CENTAUR

    override fun availableSquares(board: Board): List<Square> {
        return Knight(square, player).availableSquares(board) + board.neighborSquares(this)
    }

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Centaur(square, player)
    }
}