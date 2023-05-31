package com.idodanieli.playit.games.chess.logic


enum class Player {
    WHITE,
    BLACK;

    // opposite returns the opposite player of this
    fun opposite(): Player {
        if (this.isWhite()) {
            return BLACK
        }

        return WHITE
    }

    fun isWhite(): Boolean {
        return this == WHITE
    }

    fun isBlack(): Boolean {
        return this == BLACK
    }
}