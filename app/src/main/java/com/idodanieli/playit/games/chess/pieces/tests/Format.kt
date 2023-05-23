package com.idodanieli.playit.games.chess.pieces.tests

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece

// Contains the format that test failures will output
fun wrongMovesFormat(piece: Piece, board: Board, moves: List<Square>, msg: String): String {
    return "${msg}" +
            "${board}\n" +
            "${piece} -> ${moves}"
}

fun errorFormat(board: Board, msg: String): String {
    return "${msg}" +
            "${board}\n"
}