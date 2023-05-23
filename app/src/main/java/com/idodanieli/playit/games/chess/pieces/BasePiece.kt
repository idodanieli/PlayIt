package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.core.MovementType

open class BasePiece(override var square: Square, override val player: Player): Piece {
    override val type = ""
    override val movementType = MovementType.REGULAR

    // validMoves returns a list of the squares the piece can move to
    override fun validMoves(board: Board, ignoreCheck: Boolean, ignoreSamePlayer: Boolean): List<Square> {
        val pinner = board.getPinner(this)

        if(!ignoreCheck && board.isChecked(player)) {
            var blockingMoves = possibleCheckBlockingMoves(board)
            pinner?.let { blockingMoves = blockingMoves.intersect(square.squaresBetween(pinner.square).toSet()).toList() }

            return blockingMoves
        }

        var moves = possibleMoves(board)

        if (!ignoreSamePlayer) {
            moves = moves.filterNot { board.playerAt(it) == player }
        }

        pinner?.let {
            moves = moves.intersect(square.squaresBetween(pinner.square).toSet()).toList()
        }

        return moves
    }

    // possibleCheckBlockingMoves returns all the moves that block a check
    // TODO: I DONT LIKE THIS FUNCTION, the piece shouldn't be aware of the state of the game
    override fun possibleCheckBlockingMoves(board: Board): List<Square> {
        val moves = possibleMoves(board).filter { board.playerAt(it) != player }

        return moves.filter { move ->
            val tmpBoard = board.copy()
            tmpBoard.pieces.remove(this)
            tmpBoard.pieceAt(move)?.let {
                if(it.player != player) { tmpBoard.pieces.remove(it) }
            }

            tmpBoard.pieces.add(BasePiece(move, player))
            !tmpBoard.isChecked(player)
        }
    }

    // canBeCaptured returns true if this piece could be captured by another piece on the board
    override fun canBeCaptured(board: Board): Boolean {
        val enemyPieces = if (player == Player.BLACK) board.whitePieces else board.blackPieces
        for (enemyPiece in enemyPieces) {
            if (this.square in enemyPiece.captureMoves(board)) {
                return true
            }
        }

        return false
    }

    // possibleMoves returns all the squares a piece can move to, without taking general logic
    // into consideration like pinning, etc.
    override fun possibleMoves(board: Board): List<Square> {
        // To be overridden by child classes
        return emptyList()
    }

    override fun xrayPossibleMove(board: Board): List<Square> {
        return emptyList()
    }

    override fun captureMoves(board: Board, ignoreSamePlayer: Boolean): List<Square> {
        return validMoves(board, ignoreCheck = true, ignoreSamePlayer = ignoreSamePlayer)
    }

    override fun onMove() {
        return
    }

    override fun onEat(eatenPiece: Piece) {
        return
    }

    override fun toString(): String {
        val type = if (player == Player.WHITE) type else type.lowercase()
        return "$player $type at (${square.col}, ${square.row})"
    }
}