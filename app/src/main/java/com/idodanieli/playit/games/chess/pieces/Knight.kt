package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*
import kotlin.math.abs

class Knight(square: Square, player: Player, type: Type) : BasePiece(square, player, type) {
    // TODO: Ugly Code...
    override fun canMove(destination: Square, board: Board): Boolean {
        return abs(this.square.col - destination.col) == 2 && abs(this.square.row - destination.row) == 1 ||
                abs(this.square.col - destination.col) == 1 && abs(this.square.row - destination.row) == 2
    }
}