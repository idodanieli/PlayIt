package com.idodanieli.playit.games.chess.pieces.classic

import com.idodanieli.playit.games.chess.logic.BitBoard.Companion.NOT_A_FILE
import com.idodanieli.playit.games.chess.logic.BitBoard.Companion.NOT_EIGHTH_RANK
import com.idodanieli.playit.games.chess.logic.BitBoard.Companion.NOT_H_FILE
import com.idodanieli.playit.games.chess.logic.BitBoard.Companion.NOT_ZERO_RANK
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece

private const val MAX_START_MOVES = 2
const val TYPE_PAWN = "P"

class Pawn(square: Square, player: Player) : BasePiece(square, player) {
    override val type = TYPE_PAWN

    // TODO: Change 8 -> BOARD_SIZE
    // TODO: INSTEAD OF IS_IN USE BITBOARDS
    // TODO: CHECK IF THERE IS AN ENEMY PIECE WITH BITBOARDS
    override fun availableSquares(board: Board): List<Square> {
        val moves = arrayListOf<Square>()
        val origin = square.bitboard()

        val defaultMove: Square
        val startingMove: Square

        when(player) {
            Player.WHITE -> {
                defaultMove = Square.from_bitboard( (origin and NOT_EIGHTH_RANK) shl 8)
                startingMove = Square.from_bitboard( (origin and NOT_EIGHTH_RANK) shl 8 * MAX_START_MOVES )
            }
            Player.BLACK -> {
                defaultMove = Square.from_bitboard( (origin and NOT_ZERO_RANK) shr 8)
                startingMove = Square.from_bitboard( (origin and NOT_ZERO_RANK) shr 8 * MAX_START_MOVES )
            }
        }

        // Single square forward move
        if (board.isFree(defaultMove)) { moves.add(defaultMove) }

        // Double square forward move from the starting position
        if (!moved and board.isFree(listOf(defaultMove, startingMove))) { moves.add(startingMove) }

        moves += capturableSquares(board)

        return moves.filter{it.inBorder(board.size)}
    }

    override fun capturableSquares(board: Board): List<Square> {
        val origin = square.bitboard()
        val captureMoveLeft: Square
        val captureMoveRight: Square

        when(player) {
            Player.WHITE -> {
                captureMoveLeft = Square.from_bitboard( (origin and NOT_A_FILE) shl (8 - 1))
                captureMoveRight = Square.from_bitboard( (origin and NOT_H_FILE) shl (8 + 1))
            }
            Player.BLACK -> {
                captureMoveLeft = Square.from_bitboard( (origin and NOT_H_FILE) shr (8 - 1))
                captureMoveRight = Square.from_bitboard( (origin and NOT_A_FILE) shr (8 + 1))
            }
        }

        return listOf(captureMoveLeft, captureMoveRight)
            .filter { board.playerAt(it) == player.opposite()}
    }
}