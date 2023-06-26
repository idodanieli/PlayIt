package com.idodanieli.playit.games.chess.variants

import com.idodanieli.playit.games.chess.game_subscriber.*
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.deepCopyPieces
import com.idodanieli.playit.games.chess.pieces.*
import com.idodanieli.playit.games.chess.pieces.classic.TYPE_KING

data class Game(var name: String, private val startingPieces: Set<Piece>, var size: Int) : Publisher() {
    var board = Board(startingPieces, size)
    var currentPlayer = Player.WHITE // white always starts in chess
    var description = ""
    var started = false

    init {
        subscribe(startingPieces)
    }

    // --- Functions that change the game's state --------------------------------------------------
    fun applyMove(move: Move) {
        val piece = this.board.pieceAt(move.origin) ?: return

        if(isCaptureMove(move)) {
            applyCaptureMove(move)
        }

        board.move(piece, move.dest)

        notifySubscribers( MoveEvent(piece, move) )

        for (followUpMove in move.followUpMoves) {
            applyMove(followUpMove)
        }
    }

    fun applyAbilityMove(move: Move) {
        board.pieceAt(move.origin)?.let { piece ->
            piece.applyAbility(this)

            notifySubscribers( createAbilityActivatedEvent(piece) )
        }
    }

    private fun createAbilityActivatedEvent(piece: Piece): MoveEvent {
        val abilityActivatedMove = Move(piece.square, piece.square, isAbilityMove = true)
        return MoveEvent(piece, abilityActivatedMove)
    }

    private fun isCaptureMove(move: Move): Boolean {
        return board.pieceAt(move.dest) != null
    }

    private fun applyCaptureMove(move: Move) {
        val capturingPiece = board.pieceAt(move.origin)
        val capturedPiece = board.pieceAt(move.dest)

        applyCapture(capturingPiece!!, capturedPiece!!)
    }

    fun applyCapture(capturingPiece: Piece, capturedPiece: Piece) {
        board.remove(capturedPiece)
        capturingPiece.onCaptured(capturedPiece)
        notifySubscribers( PieceCapturedEvent(capturedPiece) )
    }

    fun switchTurn() {
        currentPlayer = currentPlayer.opposite()
    }

    // --- Functions that check the game's state ---------------------------------------------------
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

    private fun isPlayerChecked(player: Player): Boolean {
        val king = board.getPiece(TYPE_KING, player)
        king?.let { return board.canBeCaptured(it) }

        return false
    }

    // --- Move Filtering Function -----------------------------------------------------------------

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

    // --- General ---------------------------------------------------------------------------------
    fun pieces(): Set<Piece> {
        return this.board.pieces()
    }

    private fun copy(): Game {
        return Game(name, deepCopyPieces(pieces()), size)
    }
}
