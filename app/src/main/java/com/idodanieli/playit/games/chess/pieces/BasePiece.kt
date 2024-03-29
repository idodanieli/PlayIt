package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.game_subscriber.GameEvent
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.ui.views.ChessView
import com.idodanieli.playit.games.chess.ui.event_visualizers.AvailableMovesVisualizer
import com.idodanieli.playit.games.chess.ui.event_visualizers.VisualizerCollection
import com.idodanieli.playit.games.chess.variants.Game

open class BasePiece(override var square: Square, override var player: Player): Piece {
    override val type = ""
    override var moved = false

    private val visualizers = VisualizerCollection(AvailableMovesVisualizer())

    override fun visualize(chessView: ChessView) {
        visualizers.visualize(chessView)
    }

    override fun availableMoves(board: Board): List<Move> {
        // TODO: This is reallllllly not efficient -> Convert other functions to return sets
        val squares = availableSquares(board).toSet() + capturableSquares(board).toSet()
        return squares.map { dest -> Move(square, dest) }
    }

    override fun availableSquares(board: Board): List<Square> {
        throw Exception("BasePiece availableSquares called")
    }

    override fun xrayAvailableMoves(board: Board): List<Square> {
        return emptyList()
    }

    override fun capturableSquares(board: Board): List<Square> {
        return availableSquares(board)
    }

    override fun applyAbility(game: Game) {
        throw NotImplementedError("apply ability not implemented for $this")
    }

    override fun hasAbility(): Boolean {
        return false
    }

    override fun onMove() {
        this.moved = true
    }

    override fun onCaptured(capturedPiece: Piece) {}

    override fun onGameEvent(event: GameEvent) {}

    override fun toString(): String {
        val type = if (player.isWhite()) type else type.lowercase()
        return "$player $type at (${square.col}, ${square.row})"
    }

    fun isThreatened(board: Board): Boolean {
        return board.isThreatened(this.square, this.player.opposite())
    }

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        throw Exception("BasePiece copy called")
    }
}