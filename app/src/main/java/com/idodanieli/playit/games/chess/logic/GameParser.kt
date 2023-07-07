package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.pieces.*
import com.idodanieli.playit.games.chess.pieces.classic.*
import com.idodanieli.playit.games.chess.pieces.fairy.*
import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.variants.*
import org.json.JSONException
import org.json.JSONObject
import kotlin.reflect.KFunction

class GameParser {
    companion object {

        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private const val BOARD = "board"
        private const val MODE = "mode"
        private const val SIZE = "size"
        private const val EMPTY_SQUARE = '.'

        fun parse(json: JSONObject): Game {
            val name = json.getString(NAME)
            val desc = json.optString(DESCRIPTION)
            val board = json.getString(BOARD)
            val mode = json.getString(MODE)
            var size = CHESSBOARD_SIZE

            // TODO: Prettify this
            try {
                size = json.getInt(SIZE)
            } catch (e: JSONException) {
                // Do Nothing
            }

            val constructor = getGameConstructor(mode) ?: throw Exception("constructor is null for $name mode: $mode")
            val game = constructor.call(name, parseBoardPieces(board, size), size, size)
            game.description = desc

            return game
        }

        private fun getGameConstructor(mode: String): KFunction<Game>? {
            when(mode) {
                ClassicGame.TYPE -> return ClassicGame::class.constructors.first()
                CaptureOrCheckmate.TYPE -> return CaptureOrCheckmate::class.constructors.first()
                BeirutChess.TYPE -> return BeirutChess::class.constructors.first()
            }

            return null
        }

        private fun parseBoardPieces(board: String, size: Int = CHESSBOARD_SIZE): MutableSet<Piece> {
            val pieces = mutableSetOf<Piece>()

            for (row in 0 until size) {
                for (col in 0 until size) {
                    val char = board[col + row * size]
                    if (char != EMPTY_SQUARE) {
                        val square = Square(col, row)
                        val player = if (char.isUpperCase()) Player.WHITE else Player.BLACK
                        val piece = pieceFromCharacter(char.uppercase(), square, player)
                        pieces.add(piece)
                    }
                }
            }

            return pieces
        }

        // TODO: Implement this as creator so you will be able to add this code in the piece class
        fun pieceFromCharacter(char: String, square: Square, player: Player): Piece {
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
                Grasshopper.TYPE -> return Grasshopper(square, player)
                TYPE_CAMEL -> return Camel(square, player)
                TYPE_WILDBEAST -> return WildBeast(square, player)
                TYPE_AMAZON -> return Amazon(square, player)
                TYPE_EMPRESS -> return Empress(square, player)
                TYPE_ARCHBISHOP -> return Archbishop(square, player)
                TYPE_XIANGQI_HORSE -> return XiangqiHorse(square, player)
                Jester.TYPE -> return Jester(square, player)
                Terrorist.TYPE -> return Terrorist(square, player)
                Octopus.TYPE -> return Octopus(square, player)
                Spartan.TYPE -> return Spartan(square, player)
                Cannon.TYPE -> return Cannon(square, player)
            }

            return BasePiece(square, player)
        }
    }
}