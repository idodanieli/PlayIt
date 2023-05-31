package com.idodanieli.playit.games.chess

import com.idodanieli.playit.games.chess.game_listener.OnlineChessGameListener

const val CHESSBOARD_SIZE = 8

const val MODE_LOCAL = "local"
const val MODE_ONLINE = "online"
const val MODE_DEFAULT = MODE_ONLINE

val CHESS_GAME_LISTENER = mapOf(
    MODE_LOCAL to null,
    MODE_ONLINE to OnlineChessGameListener
)