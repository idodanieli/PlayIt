package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Square

interface Piece {
    var square: Square
    val player: Player
    val type: Type

    fun canMove(destination: Square, board: Board): Boolean
}

open class BasePiece(override var square: Square, override val player: Player, override val type: Type): Piece {
    override fun canMove(destination: Square, board: Board): Boolean {
        TODO("Not yet implemented")
    }
}

enum class Type {
    KING,
    QUEEN,
    ROOK,
    BISHOP,
    KNIGHT,
    PAWN,
}

enum class Player {
    WHITE,
    BLACK,
}