package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.NOT_DIAGONAL_DIRECTIONS
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.BasePiece
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.core.allSquaresInDirectionUntilPieceEncountered
import com.idodanieli.playit.games.chess.pieces.core.getSecondPieceInDirection
import com.idodanieli.playit.games.chess.ui.PieceDrawer

class Cannon(square: Square, player: Player): BasePiece(square, player) {
    companion object {
        const val TYPE = "L"
        private val DIRECTIONS = NOT_DIAGONAL_DIRECTIONS.map { it.value }

        init {
            PieceDrawer.addPiecePicture(TYPE, Player.WHITE, R.drawable.piece_cannon_white)
            PieceDrawer.addPiecePicture(TYPE, Player.BLACK, R.drawable.piece_cannon_black)
        }
    }

    override val type = TYPE

    override fun availableSquares(board: Board): List<Square> {
        return  DIRECTIONS.map { allSquaresInDirectionUntilPieceEncountered(this, board, it ) }.flatten()
    }

    override fun capturableSquares(board: Board): List<Square> {
        val pieces = DIRECTIONS.mapNotNull { getSecondPieceInDirection(this, board, it) }
        return pieces.map { it.square }
    }

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Cannon(square, player)
    }
}