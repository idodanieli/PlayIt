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
                    pieces.add(pieceFromCharacter(char.toString(), square, player))
                }
            }
        }

        return pieces
    }

    private fun pieceFromCharacter(char: String, square: Square, player: Player): Piece {
        when(char){
            TYPE_ROOK -> return Rook(square, player)
            TYPE_KNIGHT -> return Knight(square, player)
            TYPE_BISHOP -> return Bishop(square, player)
            TYPE_QUEEN -> return Queen(square, player)
            TYPE_KING -> return King(square, player)
            TYPE_PAWN -> return Pawn(square, player)
            TYPE_VENOM -> return Venom(square, player)
            TYPE_BEROLINA_PAWN -> return BerolinaPawn(square, player)
            TYPE_GIRAFFE -> return Giraffe(square, player)
            TYPE_ZEBRA -> return Zebra(square, player)
            TYPE_CENTAUR -> return Centaur(square, player)
            TYPE_ELEPHANT -> return Elephant(square, player)
            TYPE_GRASSHOPPER -> return Grasshopper(square, player)
            TYPE_CAMEL -> return Camel(square, player)
            TYPE_WILDBEAST -> return WildBeast(square, player)
            TYPE_AMAZON -> return Amazon(square, player)
            TYPE_EMPRESS -> return Empress(square, player)
        }

        return BasePiece(square, player)
    }
}