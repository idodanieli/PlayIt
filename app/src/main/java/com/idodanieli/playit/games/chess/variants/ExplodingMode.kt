package com.idodanieli.playit.games.chess.variants

import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.abilities.Bomber

class ExplodingMode(name: String, startingPieces: Set<Piece>, size: Int): ClassicGame(name, startingPieces, size) {
    init {
        val bombers = startingPieces.map { piece -> Bomber(piece) }.toSet()
        board.setPieces( bombers )
    }
}