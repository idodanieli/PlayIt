package com.idodanieli.playit.games.chess.variants

import com.idodanieli.playit.games.chess.game_subscriber.IPublisher
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.pieces.Piece

interface Game : IPublisher {
    var started: Boolean
    val currentPlayer: Player
    val board: Board
    val name: String
    var description: String

    fun getLegalMovesForPiece(piece: Piece): List<Move>
    fun applyMove(move: Move)
    fun applyAbilityMove(move: Move)
    fun applyCapture(capturingPiece: Piece, capturedPiece: Piece)
    fun switchTurn()
    fun isPlayerChecked(player: Player): Boolean
    fun isOver(): Boolean
    fun isStalemate(): Boolean
    fun pieces(): Set<Piece>

    fun isPromotionMove(move: Move): Boolean
    fun promote(piece: Piece)
}