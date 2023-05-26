package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.pieces.*
import com.idodanieli.playit.games.chess.pieces.classic.TYPE_KING

data class Game(var name: String, private var pieces: MutableSet<Piece>, var size: Int) {
    val board = Board(pieces, size)
    var currentPlayer = Player.WHITE // white always starts in chess
    var description = ""

    fun pieces(): Set<Piece> {
        return this.pieces
    }

    fun canMove(origin: Square, dst: Square): Boolean {
        if (origin == dst) {
            return  false
        }
        val movingPiece = board.pieceAt(origin) ?: return false
        if (movingPiece.player != currentPlayer) { return false }

        var moves = movingPiece.validMoves(this.board)
        if (isChecked(movingPiece.player)) {
            moves = filterBlockingMoves(movingPiece, moves)
        }

        return dst in moves
    }

    fun movePiece(origin: Square, dst: Square) {
        val piece = this.board.pieceAt(origin) ?: return

        val enemyPiece = board.pieceAt(dst, piece.player.opposite())
        enemyPiece?.let {
            board.remove(enemyPiece)
            piece.onEat(enemyPiece)
        }

        board.move(piece, dst)
    }

    fun isOver(): Boolean {
        if (isChecked(currentPlayer)) {
            for (piece in board.pieces(currentPlayer)) {
                val blockingMoves = validMoves(piece)
                if (blockingMoves.isNotEmpty()) {
                    return false
                }
            }

            return true
        }

        return false
   }

    // isChecked returns true if the given player is checked
    private fun isChecked(player: Player): Boolean {
        val king = board.piece(TYPE_KING, player)
        king?.let { return board.canBeCaptured(it) }

        return false
    }

    fun validMoves(piece: Piece): List<Square> {
        // TODO: Move logic from piece.validMoves here
        return filterBlockingMoves(piece, piece.validMoves(board))
    }

    private fun filterBlockingMoves(piece: Piece, moves: List<Square>): List<Square> {
        return moves.filter { move ->
            val tmpGame = copy()
            tmpGame.movePiece(piece.square, move)
            !tmpGame.isChecked(piece.player)
        }
    }

    private fun copy(): Game {
        return Game(name, deepCopyPieces(pieces), size)
    }
}
