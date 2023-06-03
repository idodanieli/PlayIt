package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.pieces.*
import com.idodanieli.playit.games.chess.pieces.classic.TYPE_KING

data class Game(var name: String, private val startingPieces: MutableSet<Piece>, var size: Int) {
    var board = Board(startingPieces, size)
    var currentPlayer = Player.WHITE // white always starts in chess
    var description = ""
    var started = false

    fun pieces(): Set<Piece> {
        return this.board.pieces()
    }

    // canMove returns true if the move is valid
    fun canMove(move: Move): Boolean {
        if (move.origin == move.dest) {
            return  false
        }
        val movingPiece = board.pieceAt(move.origin) ?: return false
        if (movingPiece.player != currentPlayer) { return false }

        var moves = movingPiece.validMoves(this.board)
        if (isChecked(movingPiece.player)) {
            moves = filterBlockingMoves(movingPiece, moves)
        }

        return move.dest in moves
    }

    fun movePiece(origin: Square, dst: Square) {
        val piece = this.board.pieceAt(origin) ?: return

        val enemyPiece = board.pieceAt(dst, piece.player.opposite())
        enemyPiece?.let {
            board.remove(enemyPiece)
            piece.onCaptured(enemyPiece)
        }

        board.move(piece, dst)
    }

    fun isOver(): Boolean {
        if (isChecked(currentPlayer)) {
            for (piece in board.pieces(currentPlayer)) {
                val blockingMoves = getPieceValidMoves(piece)
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
        val king = board.getPiece(TYPE_KING, player)
        king?.let { return board.canBeCaptured(it) }

        return false
    }

    fun getPieceValidMoves(piece: Piece): List<Square> {
        // TODO: Move logic from piece.validMoves here
        return filterBlockingMoves(piece, piece.validMoves(board))
    }

    fun switchTurn() {
        currentPlayer = currentPlayer.opposite()
    }

    private fun filterBlockingMoves(piece: Piece, moves: List<Square>): List<Square> {
        return moves.filter { move ->
            val tmpGame = copy()
            tmpGame.movePiece(piece.square, move)
            !tmpGame.isChecked(piece.player)
        }
    }

    private fun copy(): Game {
        return Game(name, deepCopyPieces(startingPieces), size)
    }
}
