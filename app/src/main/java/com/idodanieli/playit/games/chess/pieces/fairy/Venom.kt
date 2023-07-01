package com.idodanieli.playit.games.chess.pieces.fairy

import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.King
import com.idodanieli.playit.games.chess.pieces.core.Rider
import com.idodanieli.playit.games.chess.ui.PieceDrawer

private val MOVE_OFFSETS = arrayOf(1, -1, 0)
private const val STARTING_MAX_STEPS = 1
const val TYPE_VENOM = "U"

// Venom is a slow-moving but powerful piece that moves one square at a time in any direction (horizontally, vertically, or diagonally).
// When Venom captures an opponent's piece, it absorbs that piece's power and gains the ability to move an additional square in any direction for each subsequent move.
// For example, if Venom captures a pawn, it gains the ability to move two squares in any direction on its next move.
// If it captures another pawn on its next move, it gains the ability to move three squares in any direction on its subsequent move, and so on.
// Venom's movement and capture abilities continue to increase as it captures more pieces.
//
// In this variant, Venom piece adds an extra layer of strategy and tactical considerations, as players must weigh the risk and reward of capturing pieces with Venom.
// Will you use Venom to absorb your opponent's pieces and gain the advantage, or will you protect your pieces and limit Venom's movement options? The choice is yours!
class Venom(square: Square, player: Player) : Rider(square, player) {
    companion object {
        init {
            PieceDrawer.addPiecePicture(TYPE_VENOM, Player.WHITE, R.drawable.venom_white)
            PieceDrawer.addPiecePicture(TYPE_VENOM, Player.BLACK, R.drawable.venom_black)
        }
    }

    override val type = TYPE_VENOM
    
    private var maxSteps = STARTING_MAX_STEPS // specifies how much steps the piece can make in each direction

    override fun onCaptured(capturedPiece: Piece) {
        maxSteps++
    }

    override fun possibleMoves(board: Board, getMovesInDirection: (piece: Piece, board: Board, direction: Square, max_steps: Int) -> List<Square>): List<Square> {
        val moves = arrayListOf<Square>()

        for (i in MOVE_OFFSETS) {
            for (j in MOVE_OFFSETS) {
                if (i == 0 && j == 0) { continue }

                val direction = Square(i, j)
                moves.addAll(getMovesInDirection(this, board, direction, maxSteps))
            }
        }

        return moves
    }

    // --- General ---------------------------------------------------------------------------------
    override fun copy(): Piece {
        return Venom(square, player)
    }
}