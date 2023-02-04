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

    // onMove adds logic to piece after they have been moved
    fun onMove()
}

open class BasePiece(override var square: Square, override val player: Player, override val type: Type): Piece {
    override fun onMove() {
        return
    }

    override fun availableSquares(board: Board): List<Square> {
        TODO("Not yet implemented")
    }

    // getAllAvailableMovesInDirection returns all the available moves in the given direction
    // must be used ONLY for continuous piece like: Rook, Bishop, Queen, etc.
    fun getAllAvailableMovesInDirection(board: Board, direction: Square): List<Square> {
        val moves = arrayListOf<Square>()
        var move = square + direction

        while (board.isIn(move)) {
            when(board.playerAt(move)) {
                // a piece as same as the bishop
                player -> {
                    break
                }
                // an enemy piece
                player.opposite() -> {
                    moves.add(move)
                    break
                }
                else -> {
                    moves.add(move)
                    move += direction
                }
            }
        }

        return moves
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