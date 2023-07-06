package com.idodanieli.playit.games.chess.pieces.core

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.NO_MAX_STEPS
import com.idodanieli.playit.games.chess.pieces.Piece

// allMovesInDirection returns all the available moves in the given direction
// must be used ONLY for continuous piece like: Rook, Bishop, Queen, etc.
fun allMovesInDirection(self: Piece, board: Board, direction: Square, max_steps: Int = NO_MAX_STEPS): List<Square> {
    val moves = arrayListOf<Square>()
    var move = self.square + direction
    var steps = 0

    while (board.isIn(move) && (max_steps == NO_MAX_STEPS || steps < max_steps)) {
        when(board.playerAt(move)) {
            // a piece as same as the bishop
            self.player -> {
                moves.add(move)
                break
            }
            // an enemy piece
            self.player.opposite() -> {
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

fun allSquaresInDirectionUntilPieceEncountered(self: Piece, board: Board, direction: Square): List<Square> {
    val squares = arrayListOf<Square>()
    var currentSquare = self.square + direction

    while (board.isIn(currentSquare)) {
        if ( board.playerAt(currentSquare) != null ) break

        squares.add(currentSquare)
        currentSquare += direction
    }

    return squares
}

// xrayMovesInDirection returns all the moves in the given direction like an xray
// must be used ONLY for continuous piece like: Rook, Bishop, Queen, etc.
fun xrayMovesInDirection(self: Piece, board: Board, direction: Square, max_steps: Int = NO_MAX_STEPS): List<Square> {
    val moves = arrayListOf<Square>()
    var move = self.square + direction
    var steps = 0

    while (board.isIn(move) && (max_steps == NO_MAX_STEPS || steps < max_steps)) {
        moves.add(move)
        move += direction
        steps += 1
    }

    return moves
}

fun getFirstPieceInDirection(piece: Piece, board: Board, direction: Square): Piece? {
    var currentSquare = piece.square.copy()

    while (board.isIn(currentSquare)) {
        currentSquare += direction

        return board.pieceAt(currentSquare) ?: continue
    }

    return null
}

fun getSecondPieceInDirection(piece: Piece, board: Board, direction: Square): Piece? {
    val firstPiece = getFirstPieceInDirection(piece, board, direction) ?: return null

    return getFirstPieceInDirection(firstPiece, board, direction)
}