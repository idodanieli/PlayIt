package com.idodanieli.playit.games.chess.game_listener

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.idodanieli.playit.clients.GameClient
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.ui.ChessView

object OnlineChessGameListener: ChessGameListener {
    override fun onGameStarted(chessView: ChessView, gameID: String) {
        // TODO: Move out of here to main activity
        val player = if (GameClient.getInstance().join(gameID) == GameClient.PLAYER_WHITE) Player.WHITE else Player.BLACK
        chessView.setGameHero(player)

        showGameIDDialog(chessView.context, gameID)
    }

    override fun onPieceMoved(move: Move) {
        GameClient.getInstance().movePiece(move)
    }

    override fun canHeroPlay(chessView: ChessView): Boolean {
        return chessView.game.currentPlayer == chessView.hero
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