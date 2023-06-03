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
            moves = removeIllegalMoves(movingPiece, moves)
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

    fun switchTurn() {
        currentPlayer = currentPlayer.opposite()
    }

    // --- Move Filtering Function ------------------------------------------------------------- \\
    fun getPieceValidMoves(piece: Piece): List<Square> {
        var moves = piece.validMoves(board)

        moves = filterLegalMovesIfPinned(piece, moves)
        moves = removeFriendlyFireMoves(piece, moves)
        moves = removeIllegalMoves(piece, moves)

        return moves
    }

    // filterLegalMovesIfPinned returns all the legal moves the piece can make if being pinned by another piece
    private fun filterLegalMovesIfPinned(piece: Piece, moves: List<Square>): List<Square> {
        val pinner = board.getPinner(piece) ?: return moves

        return moves.intersect(piece.square.squaresBetween(pinner.square).toSet()).toList()
    }

    // removeFriendlyFireMoves removes moves that their destination is a friendly piece ( avoid friendly fire )
    private fun removeFriendlyFireMoves(piece: Piece, moves: List<Square>): List<Square> {
        return moves.filterNot { board.playerAt(it) == piece.player }
    }

    // removeIllegalMoves that leave the player in check
    private fun removeIllegalMoves(piece: Piece, moves: List<Square>): List<Square> {
        return moves.filterNot { move ->
            val tmpGame = copy()
            tmpGame.movePiece(piece.square, move)
            tmpGame.isChecked(piece.player)
        }
    }

    // --- General ----------------------------------------------------------------------------- \\

    private fun copy(): Game {
        return Game(name, deepCopyPieces(startingPieces), size)
    }
}
