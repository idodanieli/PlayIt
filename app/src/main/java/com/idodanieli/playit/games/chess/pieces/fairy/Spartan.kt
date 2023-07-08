package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.RestrictedPiece
import com.idodanieli.playit.games.chess.ui.drawers.PieceDrawer

class Spartan(square: Square, player: Player): RestrictedPiece(square, player) {
    companion object {
        const val TYPE = "I"

        init {
            PieceDrawer.addPiecePicture(TYPE, Player.WHITE, R.drawable.spartan_white)
            PieceDrawer.addPiecePicture(TYPE, Player.BLACK, R.drawable.spartan_black)
        }
    }

    override val type = TYPE

    override fun availableMoves(board: Board): List<Move> {
        val neighborSpartans = getNeighborSpartans(board)

        val moves = super.availableMoves(board)
        for (move in moves) {
            move.followUpMoves = moveNeighborsInSameDirection(move, board, neighborSpartans)
        }

        return moves
    }

    private fun moveNeighborsInSameDirection(move: Move, board: Board, neighbors: Collection<Piece>): Collection<Move> {
        val followUpMoves = mutableListOf<Move>()
        val direction = move.direction()

        for (neighbor in neighbors) {
            val neighborMove = Move(neighbor.square, neighbor.square + direction)

            if (isValidMove(neighbor, neighborMove, board)) {
                followUpMoves.add(neighborMove)
            }
        }

        return followUpMoves
    }

    private fun isValidMove(neighbor: Piece, move: Move, board: Board): Boolean {
        board.pieceAt(move.dest)?.let {
            return it.player != neighbor.player
        }

        return true
    }

    private fun getNeighborSpartans(board: Board): Collection<Piece> {
        return board.neighborPieces(this).filter { it.type == TYPE && it.player == this.player }
    }

    override fun availableSquares(board: Board): List<Square> {
        val squares = listOf(
            this.squareInFront(),
            this.squareBehind()
        )

        return squares.filter { board.isIn(it) }
    }

    override fun capturableSquares(board: Board): List<Square> {
        val squares = listOf(
            this.squareInFront()
        )

        return squares.filter { board.isIn(it) }
    }

    private fun squareInFront(): Square {
        if (player.isWhite()) {
            return square + Square(0, 1)
        }

        return square + Square(0, -1)
    }

    private fun squareBehind(): Square {
        if (player.isBlack()) {
            return square + Square(0, 1)
        }

        return square + Square(0, -1)
    }

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Spartan(square, player)
    }
}