package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.pieces.*
import com.idodanieli.playit.games.chess.pieces.classic.TYPE_KING

data class Game(var name: String, private val startingPieces: Set<Piece>, var size: Int) {
    var board = Board(startingPieces, size)
    var currentPlayer = Player.WHITE // white always starts in chess
    var description = ""
    var started = false

    // --- Functions that change the game's state ---------------------------------------------- \\
    fun applyMove(move: Move) {
        val piece = this.board.pieceAt(move.origin) ?: return

        val enemyPiece = board.pieceAt(move.dest, piece.player.opposite())
        enemyPiece?.let {
            board.remove(enemyPiece)
            piece.onCaptured(enemyPiece)
        }

        board.move(piece, move.dest)

        for (followUpMove in move.followUpMoves) {
            applyMove(followUpMove)
        }
    }

    fun switchTurn() {
        currentPlayer = currentPlayer.opposite()
    }

    // --- Functions that check the game's state ----------------------------------------------- \\
    fun isOver(): Boolean {
        if (isPlayerChecked(currentPlayer)) {
            for (piece in board.pieces(currentPlayer)) {
                val blockingMoves = getLegalMovesForPiece(piece)
                if (blockingMoves.isNotEmpty()) {
                    return false
                }
            }

            return true
        }

        return false
   }

    fun isPlayerChecked(player: Player): Boolean {
        val king = board.getPiece(TYPE_KING, player)
        king?.let { return board.canBeCaptured(it) }

        return false
    }

    // --- Move Filtering Function ------------------------------------------------------------- \\

    // getPieceValidMoves returns all the squares a piece can move to, while taking general logic
    // into consideration like pinning, friendly-fire etc.
    fun getLegalMovesForPiece(piece: Piece): List<Move> {
        var moves = piece.availableMoves(board)

        moves = removeFriendlyFireMoves(piece, moves)
        moves = removeIllegalMoves(piece, moves)

        return moves
    }

    // removeFriendlyFireMoves removes moves that their destination is a friendly piece ( avoid friendly fire )
    private fun removeFriendlyFireMoves(piece: Piece, moves: List<Move>): List<Move> {
        return moves.filterNot { board.playerAt(it.dest) == piece.player }
    }

    // removeIllegalMoves that leave the player in check
    private fun removeIllegalMoves(piece: Piece, moves: List<Move>): List<Move> {
        return moves.filterNot { move ->
            val tmpGame = copy()
            tmpGame.applyMove(move)
            tmpGame.isPlayerChecked(piece.player)
        }
    }

    // --- General ----------------------------------------------------------------------------- \\
    fun pieces(): Set<Piece> {
        return this.board.pieces()
    }

    private fun copy(): Game {
        return Game(name, deepCopyPieces(pieces()), size)
    }
}
