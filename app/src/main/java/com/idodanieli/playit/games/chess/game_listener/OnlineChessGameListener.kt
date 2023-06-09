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
    private lateinit var fetchEnemyMovesThread: Thread

    override fun onGameStarted(chessView: ChessView, gameID: String) {
        // TODO: Move out of here to main activity
        val player = if (GameClient.getInstance().join(gameID) == GameClient.PLAYER_WHITE) Player.WHITE else Player.BLACK
        chessView.setGameHero(player)

        fetchEnemyMovesThread = Thread { fetchEnemyMoves(chessView, Handler(getMainLooper()), interval=1000) }
        fetchEnemyMovesThread.start()

        showGameIDDialog(chessView.context, gameID)
    }

    override fun onGameOver() {
        fetchEnemyMovesThread.interrupt()
    }

    override fun onPieceMoved(move: Move) {
        GameClient.getInstance().movePiece(move)
    }

    override fun canHeroPlay(chessView: ChessView): Boolean {
        return chessView.game.currentPlayer == chessView.hero
    }

    // updateOpponentsMoves waits for the opponents moves and updates the chessView when they arrive
    private fun fetchEnemyMoves(chessView: ChessView, handler: Handler, interval: Long) = try {
        while (!fetchEnemyMovesThread.isInterrupted) {
            if (chessView.isOpponentsTurn()) {
                fetchEnemyMove(chessView, handler)
            }
            Thread.sleep(interval)
        }
    } catch (e: InterruptedException) {
        // Do nothing
    }

    private fun fetchEnemyMove(chessView: ChessView, handler: Handler) {
        val lastMove = GameClient.getInstance().getLastMove()
        if (isOpponentsMove(chessView.hero, lastMove)) {
            // Post UI-related operations to the main thread
            handler.post {
                chessView.applyMove(lastMove!!)
            }
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