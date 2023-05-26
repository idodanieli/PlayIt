package com.idodanieli.playit.games.chess.game_listener

import android.util.Log
import com.idodanieli.playit.clients.GameClient
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.ui.ChessView

object OnlineChessGameListener: ChessGameListener {
    override fun onGameStarted(chessView: ChessView) {
        val gameID = GameClient.getInstance().create()
        Log.d("GameClient", "Created game: $gameID")

        val player = if (GameClient.getInstance().join(gameID) == "WHITE") Player.WHITE else Player.BLACK
        chessView.setGameHero(player)
    }

    override fun onPieceMoved(move: Move) {
        GameClient.getInstance().movePiece(move)
    }

    override fun onTurnSwitched(chessview: ChessView) {
        // TODO: For local play do nothing
        Thread.sleep(5) // TODO: Eliminate race condition
        val lastMove = GameClient.getInstance().getLastMove()
        Log.d("GameClient", "LastMove: $lastMove")
    }

    override fun canHeroPlay(chessView: ChessView): Boolean {
        return chessView.game.currentPlayer == chessView.hero
    }
}