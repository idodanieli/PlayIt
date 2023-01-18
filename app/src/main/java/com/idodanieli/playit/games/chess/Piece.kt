package com.idodanieli.playit.games.chess

data class Piece(val square: Square, val player: Player, val type: Type) {
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