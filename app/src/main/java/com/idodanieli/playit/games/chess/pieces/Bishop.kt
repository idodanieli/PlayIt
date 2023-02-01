package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Square
import kotlin.math.abs

class Bishop(square: Square, player: Player, type: Type) : BasePiece(square, player, type) {

    override fun canMove(destination: Square, board: Board): Boolean {
       return canBishopMove(this.square, destination, board)
    }
}

fun canBishopMove(source: Square, destination: Square, board: Board): Boolean {
    if (abs(source.col - destination.col) == abs(source.row - destination.row)) {
        return board.isClearDiagonally(source, destination)
    }
    return false
}