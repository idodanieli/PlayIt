package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.*

private const val WHITE_DIRECTION = 1
private const val BLACK_DIRECTION = -1
private const val MAX_START_MOVES = 2
const val TYPE_PAWN = "P"

class Pawn(square: Square, player: Player) : BasePiece(square, player) {
    override val type = TYPE_PAWN
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
            val move = Square(square.col, square.row + direction * MAX_START_MOVES)
            if (board.isFree(move)) { moves.add(move) }
        }

        val defaultMove = Square(square.col, square.row + direction)
        if (board.isIn(defaultMove) && board.isFree(defaultMove)) { moves.add(defaultMove) }


        for (move in eatMoves(board)) {
            // There is an enemy piece at the given square
            board.pieceAt(move)?.let {
                if (it.player != player) { moves.add(move) }
            }
        }

        return moves
    }

    override fun eatMoves(board: Board, ignoreSamePlayer: Boolean): List<Square> {
        val origin = square.bitboard()

        val eatMoveLeft = origin shl (8 - 1) and BitBoard.NOT_A_FILE
        val eatMoveRight = origin shl (8 + 1) and BitBoard.NOT_H_FILE

        return listOf(eatMoveLeft, eatMoveRight)
            .map { Square.from_bitboard(it) }
            .filter { it.isValid(board.size) }
    }

    // TODO: Remove this and make a isOnStartingSquare function instead...
    override fun onMove() {
        super.onMove()

        this.moved = true
    }
}