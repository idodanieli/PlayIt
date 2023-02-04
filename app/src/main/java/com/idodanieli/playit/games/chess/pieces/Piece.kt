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

    // onEat adds logic to piece after they have eaten another piece
    fun onEat(eatenPiece: Piece)
}

open class BasePiece(override var square: Square, override val player: Player): Piece {
    override val type = Type.NONE

    override fun onMove() {
        return
    }

    override fun availableSquares(board: Board): List<Square> {
        TODO("Not yet implemented")
    }

    override fun onEat(eatenPiece: Piece) {
        return
    }

    override fun toString(): String {
        return "$player $type at (${square.col}, ${square.row})"
    }

    // getAllAvailableMovesInDirection returns all the available moves in the given direction
    // must be used ONLY for continuous piece like: Rook, Bishop, Queen, etc.
    fun getAllAvailableMovesInDirection(board: Board, direction: Square, max_steps: Int = 0): List<Square> {
        val moves = arrayListOf<Square>()
        var move = square + direction
        var steps = 0

        while (board.isIn(move) && (max_steps == 0 || steps < max_steps)) {
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
                    steps += 1
                }
            }
        }

        return moves
    }
}

enum class Type {
    NONE,
    KING,
    QUEEN,
    ROOK,
    BISHOP,
    KNIGHT,
    PAWN,
    VENOM, // TODO: Change to parasite?
}