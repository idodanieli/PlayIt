package com.idodanieli.playit.games.chess.game_subscriber

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Looper.getMainLooper
import android.os.Handler
import android.view.View
import com.idodanieli.playit.User
import com.idodanieli.playit.clients.GameClient
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.ui.ChessView

object OnlineChessSubscriber: GameSubscriber {
    private lateinit var fetchEnemyMovesThread: Thread

    // --- Subscriber ------------------------------------------------------------------------------
    override fun onGameEvent(event: GameEvent) {
        when(event) {
            is GameSelectedEvent -> {
                joinGame(event.chessView, event.gameID)
                waitForOpponentToJoin(event.chessView)
            }

            is GameStartedEvent -> {
                fetchEnemyMoveInTheBackground(event.chessView)
            }

            is GameOverEvent -> {
                fetchEnemyMovesThread.interrupt()
            }

            is MoveEvent -> {
                GameClient.getInstance().movePiece(event.move)
            }

            is AbilityActivatedEvent -> {

            }
        }
    }

    // --- Game Selected ---------------------------------------------------------------------------
    private fun joinGame(chessView: ChessView, gameID: String) {
        val gameClient = GameClient.getInstance()

        val player = if (gameClient.join(gameID) == GameClient.PLAYER_WHITE) Player.WHITE else Player.BLACK
        chessView.setGameHero(player)
    }

    private fun waitForOpponentToJoin(chessView: ChessView) {
        val gameClient = GameClient.getInstance()

        val dialog = createWaitingForOpponentDialog(chessView.context, gameClient.gameID)
        dialog.show()

        Thread {
            val user = User.getInstance().getUsername()
            val opponent = gameClient.getOpponent()

            Handler(getMainLooper()).post{
                chessView.setPlayers(user, opponent)
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

    // --- Game Started ----------------------------------------------------------------------------
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
        if (lastMove != null && chessView.isOpponentsMove(lastMove)) {
            // Post UI-related operations to the main thread
            handler.post {
                chessView.applyMove(lastMove)
            }
        }
    }
}