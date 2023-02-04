package com.idodanieli.playit.games.chess

import com.idodanieli.playit.games.chess.pieces.*
import org.json.JSONObject

private const val NAME = "name"
private const val BOARD = "board"
private const val EMPTY_SQUARE = '.'
private const val WHITE_LAST_ROW = 2

class GameParser {
    fun parse(json: JSONObject): Game {
        val name = json.getString(NAME)
        val board = json.getString(BOARD)

        return Game(name, parseBoardPieces(board), CHESSBOARD_SIZE)
    }

    private fun parseBoardPieces(board: String): MutableSet<Piece> {
        val pieces = mutableSetOf<Piece>()

        for (row in 0 until CHESSBOARD_SIZE) {
            for (col in 0 until CHESSBOARD_SIZE) {
                val char = board[col + row * CHESSBOARD_SIZE]
                if (char != EMPTY_SQUARE) {
                    val square = Square(col, row)
                    val player = if (row < WHITE_LAST_ROW) Player.WHITE else Player.BLACK
                    pieces.add(pieceFromCharacter(char, square, player))
                }
            }
        }

        return pieces
    }

    private fun pieceFromCharacter(char: Char, square: Square, player: Player): Piece {
        when(char){
            'R' -> return Rook(square, player)
            'k' -> return Knight(square, player)
            'B' -> return Bishop(square, player)
            'Q' -> return Queen(square, player)
            'K' -> return King(square, player)
            'P' -> return Pawn(square, player)
            'V' -> return Venom(square, player)
        }

        return BasePiece(square, player)
    }
}