package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Player
import com.idodanieli.playit.games.chess.Square

class Centaur(square: Square, player: Player) : BasePiece(square, player) {
    override val type = Type.CENTAUR
    override val movementType = MovementType.LEAPER

    override fun possibleMoves(board: Board): List<Square> {
        return Knight(square, player).possibleMoves(board) + board.getAvailableNeighborSquares(this)
    }
}