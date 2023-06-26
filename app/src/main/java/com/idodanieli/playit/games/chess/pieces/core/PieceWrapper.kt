package com.idodanieli.playit.games.chess.pieces.core

import com.idodanieli.playit.games.chess.game_subscriber.GameEvent
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.TouchData
import com.idodanieli.playit.games.chess.variants.Game

open class PieceWrapper(val piece: Piece): Piece {

    override var square: Square
        get() = piece.square
        set(value) {
            piece.square = value
        }

    override var moved: Boolean
        get() = piece.moved
        set(value) {
            piece.moved = value
        }

    override var player: Player
        get() = piece.player
        set(value) {
            piece.player = value
        }

    override val type: String
        get() = piece.type

    override fun onGameEvent(event: GameEvent) {
        return piece.onGameEvent(event)
    }

    override fun onCaptured(capturedPiece: Piece) {
        return piece.onCaptured(capturedPiece)
    }

    override fun onMove() {
        piece.onMove()
    }

    override fun visualize(touch: TouchData?, chessView: ChessView) {
        piece.visualize(touch, chessView)
    }

    override fun availableMoves(board: Board): List<Move> {
        return piece.availableMoves(board)
    }

    override fun availableSquares(board: Board): List<Square> {
        return piece.availableSquares(board)
    }

    override fun capturableSquares(board: Board): List<Square> {
        return piece.capturableSquares(board)
    }

    override fun xrayAvailableMoves(board: Board): List<Square> {
        return piece.xrayAvailableMoves(board)
    }

    override fun applyAbility(game: Game) {
        piece.applyAbility(game)
    }

    override fun hasAbility(): Boolean {
        return piece.hasAbility()
    }

    override fun copy(): Piece {
        return PieceWrapper(piece.copy())
    }
}