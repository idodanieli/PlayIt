package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.game_subscriber.GameEvent
import com.idodanieli.playit.games.chess.game_subscriber.MoveEvent
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.ChessDrawer

class Jester(square: Square, player: Player) : BasePiece(square, player) {
    companion object {
        const val TYPE = "J"

        init {
            ChessDrawer.addPiecePicture(TYPE, Player.WHITE, R.drawable.jester_white)
            ChessDrawer.addPiecePicture(TYPE, Player.BLACK, R.drawable.jester_black)
        }
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
                    mimickedPiece = createMimickedPiece(event.movedPiece)
                }
            }
        }
    }

    private fun createMimickedPiece(piece: Piece): Piece {
        val copiedPiece = piece.copy()
        copiedPiece.square = this.square
        copiedPiece.player = this.player
        copiedPiece.moved = this.moved

        return  copiedPiece
    }

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Jester(square, player)
    }
}