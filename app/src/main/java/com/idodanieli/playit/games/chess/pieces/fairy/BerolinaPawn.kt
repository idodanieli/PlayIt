package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.core.MovementType

private const val WHITE_DIRECTION = 1
private const val BLACK_DIRECTION = -1
private const val MAX_START_MOVES = 2
const val TYPE_BEROLINA_PAWN = "V"

class BerolinaPawn(square: Square, player: Player) : BasePiece(square, player) {
    override val type = TYPE_BEROLINA_PAWN
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


        val captureMove = captureMove()
        // There is an enemy piece at the given square
        board.pieceAt(captureMove)?.let {
            if (it.player != player) { possibleMoves.add(captureMove) }
        }

        return possibleMoves
    }

    override fun captureMoves(board: Board): List<Square> {
        return listOf(captureMove()).filter { it.inBorder(board.size) }
    }

    private fun captureMove(): Square {
        return Square(square.col, square.row + direction)
    }

    // TODO: Remove this and make a isOnStartingSquare function instead...
    override fun onMove() {
        super.onMove()

        this.moved = true
    }
}