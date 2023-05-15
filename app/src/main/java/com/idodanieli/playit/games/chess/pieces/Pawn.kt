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

    // TODO: Change 8 -> BOARD_SIZE
    // TODO: INSTEAD OF IS_IN USE BITBOARDS
    // TODO: CHECK IF THERE IS AN ENEMY PIECE WITH BITBOARDS
    override fun possibleMoves(board: Board): List<Square> {
        val moves = arrayListOf<Square>()

        val origin = square.bitboard()

        val defaultMove: Square
        val startingMove: Square

        when(player) {
            Player.WHITE -> {
                defaultMove = Square.from_bitboard( origin shl 8 )
                startingMove = Square.from_bitboard( origin shl 8 * MAX_START_MOVES )
            }
            Player.BLACK -> {
                defaultMove = Square.from_bitboard( origin shr 8 )
                startingMove = Square.from_bitboard( origin shr 8 * MAX_START_MOVES )
            }
        }

        // Single square forward move
        if (board.isIn(defaultMove) && board.isFree(defaultMove)) { moves.add(defaultMove) }

        // Double square forward move from the starting position
        if (!moved) {
            if (board.isFree(startingMove)) { moves.add(startingMove) }
        }


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
        val eatMoveLeft: Square
        val eatMoveRight: Square

        when(player) {
            Player.WHITE -> {
                eatMoveLeft = Square.from_bitboard( origin shl (8 - 1) and BitBoard.NOT_A_FILE )
                eatMoveRight = Square.from_bitboard( origin shl (8 + 1) and BitBoard.NOT_H_FILE )
            }
            Player.BLACK -> {
                eatMoveLeft = Square.from_bitboard( origin shr (8 - 1) and BitBoard.NOT_A_FILE )
                eatMoveRight = Square.from_bitboard( origin shr (8 + 1) and BitBoard.NOT_H_FILE )
            }
        }


        return listOf(eatMoveLeft, eatMoveRight)
            .filter { it.isValid(board.size) }
    }

    // TODO: Remove this and make a isOnStartingSquare function instead...
    override fun onMove() {
        super.onMove()

        this.moved = true
    }
}