package com.idodanieli.playit.games.chess.variants

import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.abilities.Bomber

class BeirutChess(name: String, startingPieces: Set<Piece>, size: Int): CaptureOrCheckmate(name, startingPieces, size) {
    companion object {
        const val TYPE = "beirut"
    }

    init {
        // TODO: Sync bombers in Online Mode - This results in: kotlin.NotImplementedError: apply ability not implemented for WHITE N at (3, 7)
        val wPiece = board.getRandomPieceOfPlayer(Player.WHITE)
        val bPiece = board.getRandomPieceOfPlayer(Player.BLACK)

        board.remove(wPiece)
        board.remove(bPiece)

        board.add(Bomber(wPiece))
        board.add(Bomber(bPiece))
    }
}