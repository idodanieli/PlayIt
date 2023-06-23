package com.idodanieli.playit.games.chess.pieces.classic

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.MovementType

const val TYPE_KING = "K"

private const val CASTLE_MOVE_AMOUNT = 2

open class King(square: Square, player: Player) : BasePiece(square, player) {
    override val type = TYPE_KING
    override val movementType = MovementType.LEAPER

    override fun availableSquares(board: Board): List<Square> {
        return availableMoves(board).map { it.dest }
    }

    override fun availableMoves(board: Board): List<Move> {
        val moves = getNeighborMoves(board)

        if (this.canCastle(board)) {
            moves += getCastlingMoves(board)
        }

        return moves.filter { !board.isThreatened(it.dest, player.opposite()) }
    }

    private fun getNeighborMoves(board: Board): MutableList<Move> {
        return getNeighborSquares(board).map { square -> Move(this.square, square) } as MutableList<Move>
    }

    private fun getNeighborSquares(board: Board): List<Square> {
        return board.neighborSquares(this)
    }

    // --- Castling Logic ---------------------------------------------------------------------- \\
    fun getCastlingMoves(board: Board): List<Move> {
        val moves = mutableListOf<Move>()

        val friendlyRooks = board.pieces(player).filter { it.type == TYPE_ROOK }
        for (rook in friendlyRooks) {
            if (this.canCastleWith(rook, board)) {
                moves.add(this.getCastlingMove(rook))
            }
        }

        return moves
    }

    // getCastlingMove returns the square the king would land on after castling with the given piece
    private fun getCastlingMove(rook: Piece): Move {
        val kingOrigin = square
        val rookOrigin = rook.square

        val kingDest: Square
        val rookDest: Square

        if (rook.square.col > square.col) {
            kingDest = kingOrigin + Square(CASTLE_MOVE_AMOUNT, 0)
            rookDest = kingDest - Square(1, 0)

        } else {
            kingDest = kingOrigin - Square(CASTLE_MOVE_AMOUNT, 0)
            rookDest = kingDest + Square(1, 0)
        }

        val pieceMove = Move(rookOrigin, rookDest)

        return Move(kingOrigin, kingDest, followUpMoves = listOf(pieceMove))
    }

    // canCastleWith returns true if the king can castle with the given piece
    fun canCastleWith(piece: Piece, board: Board): Boolean {
        // Cant castle with a piece that has already been moved
        if(piece.moved) { return false }

        // Example: K . . R -> Will return the squares between the king and the rook ( the dots )
        val squaresBetweenPieces = square.squaresBetween(piece.square, excludeDestination = true)
        for (currentSquare in squaresBetweenPieces) {
            if (!board.isFreeAndSafe(currentSquare, enemy= piece.player.opposite())) {
                // The castling is either blocked by another piece, or illegal because the square
                // is threatened by another piece
                return false
            }
        }

        return true
    }

    fun canCastle(board: Board): Boolean {
        return !moved && !isThreatened(board)
    }
}