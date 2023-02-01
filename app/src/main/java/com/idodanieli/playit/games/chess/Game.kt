package com.idodanieli.playit.games.chess

import com.idodanieli.playit.games.chess.pieces.*

data class Game(var pieces: MutableSet<Piece>) {
    val board = Board(pieces)

    fun pieces(): Set<Piece> {
        return this.pieces
    }
}

// classicPiecesSet adds all the pieces to the board at the classic correct order
fun classicPiecesSet(): MutableSet<Piece> {
    val pieces = mutableSetOf<Piece>()

    for (i in 0 until 2) {
        pieces.add(Rook(Square(0 + i * 7, 0), Player.WHITE, Type.ROOK))
        pieces.add(Rook(Square(0 + i * 7, 7), Player.BLACK, Type.ROOK))

        pieces.add(Knight(Square(1 + i * 5, 0), Player.WHITE, Type.KNIGHT))
        pieces.add(Knight(Square(1 + i * 5, 7), Player.BLACK, Type.KNIGHT))

        pieces.add(Bishop(Square(2 + i * 3, 0), Player.WHITE, Type.BISHOP))
        pieces.add(Bishop(Square(2 + i * 3, 7), Player.BLACK, Type.BISHOP))
    }

    for (i in 0 until 8) {
        pieces.add(Pawn(Square(i, 1), Player.WHITE, Type.PAWN))
        pieces.add(Pawn(Square(i, 6), Player.BLACK, Type.PAWN))
    }

    pieces.add(Queen(Square(3, 0), Player.WHITE, Type.QUEEN))
    pieces.add(Queen(Square(3, 7), Player.BLACK, Type.QUEEN))
    pieces.add(King(Square(4, 0), Player.WHITE, Type.KING))
    pieces.add(King(Square(4, 7), Player.BLACK, Type.KING))

    return pieces
}