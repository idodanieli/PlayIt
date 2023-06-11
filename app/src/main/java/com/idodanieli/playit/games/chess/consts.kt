package com.idodanieli.playit.games.chess

import com.idodanieli.playit.games.chess.game_subscriber.LocalChessGameSubscriber
import com.idodanieli.playit.games.chess.game_subscriber.OnlineChessSubscriber

const val CHESSBOARD_SIZE = 8

const val MODE_LOCAL = "local"
const val MODE_ONLINE = "online"
const val MODE_DEFAULT = MODE_ONLINE

val MODE_TO_GAME_SUBSCRIBER = mapOf(
    MODE_LOCAL to LocalChessGameSubscriber,
    MODE_ONLINE to OnlineChessSubscriber
)