package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Square

class Pawn(square: Square, player: Player, type: Type) : BasePiece(square, player, type) {
    // TODO: UGLY CODE
    override fun canMove(destination: Square, board: Board): Boolean {
        if (this.square.col == destination.col) {
            if (this.square.row == 1) {
                return destination.row == 2 || destination.row == 3
            } else if (this.square.row == 6) {
                return destination.row == 5 || destination.row == 4
            }
        }
        return false
    }
}