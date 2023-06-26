package com.idodanieli.playit.games.chess.variants

import com.idodanieli.playit.games.chess.game_subscriber.IPublisher
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.pieces.Piece

interface Game : IPublisher {
    val size: Int
    var started: Boolean
    val currentPlayer: Player
    val board: Board
    val name: String
    val description: String

    fun applyMove(move: Move)
    fun applyAbilityMove(move: Move)
    fun applyCapture(capturingPiece: Piece, capturedPiece: Piece)
    fun switchTurn()
    fun isOver(): Boolean
    fun getLegalMovesForPiece(piece: Piece): List<Move>
    fun pieces(): Set<Piece>
}