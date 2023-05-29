package com.idodanieli.playit.games.chess.game_listener

import android.app.AlertDialog
import android.content.Context
import android.os.Looper.getMainLooper
import android.os.Handler
import com.idodanieli.playit.clients.GameClient
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.ui.ChessView

object OnlineChessGameListener: ChessGameListener {

    override fun onGameStarted(chessView: ChessView, gameID: String) {
        // TODO: Move out of here to main activity
        val player = if (GameClient.getInstance().join(gameID) == GameClient.PLAYER_WHITE) Player.WHITE else Player.BLACK
        chessView.setGameHero(player)

        Thread {
            updateOpponentsMoves(chessView, Handler(getMainLooper()), interval=1000)
        }.start()

        showGameIDDialog(chessView.context, gameID)
    }

    override fun onPieceMoved(move: Move) {
        GameClient.getInstance().movePiece(move)
    }

    override fun canHeroPlay(chessView: ChessView): Boolean {
        return chessView.game.currentPlayer == chessView.hero
    }

    // updateOpponentsMoves waits for the opponents moves and updates the chessView when they arrive
    private fun updateOpponentsMoves(chessView: ChessView, handler: Handler, interval: Long) {
        while (true) {
            if (chessView.isOpponentsTurn()) {
                val lastMove = GameClient.getInstance().getLastMove()
                if (isOpponentsMove(chessView.hero, lastMove)) {
                    // Post UI-related operations to the main thread
                    handler.post {
                        chessView.movePiece(lastMove!!)
                    }
                }
            }

            Thread.sleep(interval)
        }
    }

    private fun isOpponentsMove(hero: Player, move: Move?): Boolean {
        move?.let {
            return move.player != hero
        }

        return false
    }

    private fun showGameIDDialog(context: Context, gameID: String) {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setTitle("Game Created")
        dialogBuilder.setMessage("Game ID: $gameID")

        // Create and show the dialog
        val dialog = dialogBuilder.create()
        dialog.show()
    }
}