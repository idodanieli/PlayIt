package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.pieces.*
import com.idodanieli.playit.games.chess.pieces.classic.*
import com.idodanieli.playit.games.chess.pieces.fairy.*
import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import org.json.JSONObject

class GameParser {
    companion object {

        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private const val BOARD = "board"
        private const val EMPTY_SQUARE = '.'

        fun parse(json: JSONObject): Game {
            val name = json.getString(NAME)
            val desc = json.optString(DESCRIPTION)
            val board = json.getString(BOARD)

            val game = Game(name, parseBoardPieces(board), CHESSBOARD_SIZE)
            game.description = desc

            return game
        }

        private fun parseBoardPieces(board: String): MutableSet<Piece> {
            val pieces = mutableSetOf<Piece>()

            for (row in 0 until CHESSBOARD_SIZE) {
                for (col in 0 until CHESSBOARD_SIZE) {
                    val char = board[col + row * CHESSBOARD_SIZE]
                    if (char != EMPTY_SQUARE) {
                        val square = Square(col, row)
                        val player = if (char.isUpperCase()) Player.WHITE else Player.BLACK
                        pieces.add(pieceFromCharacter(char.uppercase(), square, player))
                    }
                }
            }

            return pieces
        }

        // TODO: Implement this as creator so you will be able to add this code in the piece class
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
                TYPE_ARCHBISHOP -> return Archbishop(square, player)
                TYPE_XIANGQI_HORSE -> return XiangqiHorse(square, player)
                Jester.TYPE -> return Jester(square, player)
                Terrorist.TYPE -> return Terrorist(square, player)
            }

            return BasePiece(square, player)
        }
    }
}