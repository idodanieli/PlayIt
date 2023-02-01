package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Square

class Rook(square: Square, player: Player, type: Type) : BasePiece(square, player, type) {

    override fun canMove(destination: Square, board: Board): Boolean {
        return canRookMove(this.square, destination, board)
    }
}

fun canRookMove(source: Square, destination: Square, board: Board): Boolean {
    if (source.col == destination.col && board.isClearVerticallyBetween(source, destination) ||
        source.row == destination.row && board.isClearHorizontallyBetween(source, destination)) {
        return true
    }
    return false
}