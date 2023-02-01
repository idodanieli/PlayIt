package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Square

class Queen(square: Square, player: Player, type: Type) : BasePiece(square, player, type) {

    override fun canMove(destination: Square, board: Board): Boolean {
        return canQueenMove(this.square, destination, board)
    }
}

fun canQueenMove(source: Square, destination: Square, board: Board): Boolean {
    return canRookMove(source, destination, board) || canBishopMove(source, destination, board)
}