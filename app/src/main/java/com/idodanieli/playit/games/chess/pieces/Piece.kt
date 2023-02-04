package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Player
import com.idodanieli.playit.games.chess.Square

interface Piece {
    var square: Square
    val player: Player
    val type: Type

    // availableSquares returns a list of the squares the piece can move to
    fun availableSquares(board: Board): List<Square>

    // canMove returns true if the piece can move into the given square on the board
    fun canMove(destination: Square, board: Board): Boolean

    // onMove adds logic to piece after they have been moved
    fun onMove()
}

open class BasePiece(override var square: Square, override val player: Player, override val type: Type): Piece {
    override fun canMove(destination: Square, board: Board): Boolean {
        TODO("Not yet implemented")
    }

    override fun onMove() {
        return
    }

    override fun availableSquares(board: Board): List<Square> {
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