package com.idodanieli.playit.games.chess

data class Piece(val col: Int, val row: Int, val player: Player, val type: Type) {
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