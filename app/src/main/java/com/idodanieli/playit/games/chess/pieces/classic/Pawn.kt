package com.idodanieli.playit.games.chess.pieces.classic

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.BitBoard.Companion.NOT_A_FILE
import com.idodanieli.playit.games.chess.logic.BitBoard.Companion.NOT_H_FILE
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.drawers.PieceDrawer

class Pawn(square: Square, player: Player) : BasePiece(square, player) {
    companion object {
        private const val MAX_START_MOVES = 2

        const val TYPE = "P"

        init {
            PieceDrawer.addPiecePicture(TYPE, Player.WHITE, R.drawable.pawn_white)
            PieceDrawer.addPiecePicture(TYPE, Player.BLACK, R.drawable.pawn_black)
        }
    }

    override val type = TYPE

    override fun availableSquares(board: Board): List<Square> {
        val moves = arrayListOf<Square>()

        val direction = if (player.isWhite()) Square(0, 1) else Square(0, -1)

        val defaultMove = square + direction
        val startingMove = square + direction * MAX_START_MOVES

        if (board.isFree(defaultMove)) { moves.add(defaultMove) }

        // Double square forward move from the starting position
        if (!moved and board.isFree(listOf(defaultMove, startingMove))) { moves.add(startingMove) }

        return moves.filter{ board.isIn(it) }
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

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Pawn(square, player)
    }
}