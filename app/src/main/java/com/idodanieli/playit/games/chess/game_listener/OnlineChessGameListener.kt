package com.idodanieli.playit.games.chess.game_listener

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Looper.getMainLooper
import android.os.Handler
import android.view.View
import com.idodanieli.playit.clients.GameClient
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.ui.ChessView

object OnlineChessGameListener: ChessGameListener, GameSubscriber {
    private lateinit var fetchEnemyMovesThread: Thread

    // --- Subscriber ------------------------------------------------------------------------------
    override fun onGameEvent(event: GameEvent) {
        when(event) {
            is GameStartedEvent -> {
                fetchEnemyMoveInTheBackground(event.chessView)
            }

            is GameOverEvent -> {
                fetchEnemyMovesThread.interrupt()
            }
        }
    }

    // --- OnGameSelected --------------------------------------------------------------------------
    override fun onGameSelected(chessView: ChessView, gameID: String) {
        val gameClient = GameClient.getInstance()

        val player = if (gameClient.join(gameID) == GameClient.PLAYER_WHITE) Player.WHITE else Player.BLACK
        chessView.setGameHero(player)

        chessView.subscribe(this)

        waitForOpponentToJoin(chessView)
    }

    private fun waitForOpponentToJoin(chessView: ChessView) {
        val gameClient = GameClient.getInstance()

        val dialog = createWaitingForOpponentDialog(chessView.context, gameClient.gameID)
        dialog.show()

        Thread {
            Handler(getMainLooper()).post{
                chessView.opponentTextView.text = gameClient.getOpponent()
                chessView.opponentTextView.visibility = View.VISIBLE
                chessView.heroTextView.visibility = View.VISIBLE
                dialog.cancel()
            }

            chessView.startGame()
        }.start()
    }

    private fun createWaitingForOpponentDialog(context: Context, gameID: String): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setTitle("Game Created")
        dialogBuilder.setCancelable(false)
        dialogBuilder.setMessage(
            "Game ID: $gameID\n" +
                    "Waiting for opponent"
        )

        return dialogBuilder.create()
    }

    override fun onPieceMoved(move: Move) {
        GameClient.getInstance().movePiece(move)
    }

    override fun canHeroPlay(chessView: ChessView): Boolean {
        return chessView.game.currentPlayer == chessView.hero
    }

    private fun fetchEnemyMoveInTheBackground(chessView: ChessView) {
        fetchEnemyMovesThread = Thread { fetchEnemyMoves(chessView, Handler(getMainLooper()), interval=GameClient.DEFAULT_SLEEP_INTERVAL) }
        fetchEnemyMovesThread.start()
    }
    private fun fetchEnemyMoves(chessView: ChessView, handler: Handler, interval: Long) {
        try {
            while (!fetchEnemyMovesThread.isInterrupted) {
                if (chessView.isOpponentsTurn()) {
                    fetchEnemyMove(chessView, handler)
                }
                Thread.sleep(interval)
            }
        } catch (e: InterruptedException) {
            // Do nothing
        }
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
}