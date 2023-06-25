package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.King

class Jester(square: Square, player: Player) : BasePiece(square, player) {
    companion object {
        const val TYPE = "J"
    }

    override val type = TYPE

    private var mimickedPiece: Piece? = null

    override fun availableSquares(board: Board): List<Square> {
        mimickedPiece?.let {
            return it.availableSquares(board)
        }

        return emptyList()
    }

    override fun onGameEvent(event: GameEvent) {
        when(event) {
            is MoveEvent -> {
                if (this.player != event.movedPiece.player) {
                    mimickedPiece = event.movedPiece.copy()
                }
            }
        }
    }

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Jester(square, player)
    }
}