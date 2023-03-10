package com.idodanieli.playit.games.chess


enum class Player {
    WHITE,
    BLACK;

    // opposite returns the opposite player of this
    fun opposite(): Player {
        if (this == WHITE) {
            return BLACK
        }

        return WHITE
    }
}