package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private const val WHITE_DIRECTION = 1
private const val BLACK_DIRECTION = -1
private const val MAX_START_MOVES = 2

class BerolinaPawn(square: Square, player: Player) : BasePiece(square, player) {
    override val type = Type.BEROLINA
    override val movementType = MovementType.LEAPER

    var direction = WHITE_DIRECTION
    var moved = false // specifies whether the pawn has been moved from its starting square

    init {
        if (player == Player.BLACK) {
            direction = BLACK_DIRECTION
        }
    }

    override fun possibleMoves(board: Board): List<Square> {
        val moves = arrayListOf<Square>()
        if (!moved) {
            val move1 = Square(square.col + direction * MAX_START_MOVES, square.row + direction * MAX_START_MOVES)
            val move2 = Square(square.col - direction * MAX_START_MOVES, square.row + direction * MAX_START_MOVES)
            moves.addAll(listOf(move1, move2))
        }

        val defaultMove1 = Square(square.col + direction, square.row + direction)
        val defaultMove2 = Square(square.col - direction, square.row + direction)
        moves.addAll(listOf(defaultMove1, defaultMove2))

        val possibleMoves = arrayListOf<Square>()
        possibleMoves.addAll(moves.filter { board.isIn(it) && board.isFree(it) })


        val eatMove = eatMove()
        // There is an enemy piece at the given square
        board.pieceAt(eatMove)?.let {
            if (it.player != player) { possibleMoves.add(eatMove) }
        }

        return possibleMoves
    }

    override fun eatMoves(board: Board, ignoreSamePlayer: Boolean): List<Square> {
        return listOf(eatMove()).filter { it.isValid(board.size) }
    }

    private fun eatMove(): Square {
        return Square(square.col, square.row + direction)
    }

    // TODO: Remove this and make a isOnStartingSquare function instead...
    override fun onMove() {
        super.onMove()

        this.moved = true
    }
}