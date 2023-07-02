package com.idodanieli.playit.games.chess.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import com.idodanieli.playit.games.chess.logic.Player

class DialogBuilder(val context: Context) {

    fun showGameOverDialog(winner: Player?, onPositiveButton: Runnable) {
        val message = if (winner != null) "$winner is the winner!"  else "Stalemate!"
        val dialog = createDialog("Game Over", message, "New Game", onPositiveButton,"Go Back")

        dialog.show()
    }

    fun showAreYouSureYouWantToQuitDialog(quitRunnable: Runnable) {
        val dialog = createDialog(
            title = "Are you sure you want to quit?",
            message = "This will count as a loss!",
            positiveText = "Yes I Am Sure",
            negativeText = "Cancel",
            positiveRunnable = quitRunnable
        )

        dialog.show()
    }

    private fun createDialog(title: String, message: String, positiveText: String, positiveRunnable: Runnable, negativeText: String): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)

        dialogBuilder.setPositiveButton(positiveText) { dialog, _ ->
            positiveRunnable.run()
            dialog.dismiss()
        }

        dialogBuilder.setNegativeButton(negativeText) { dialog, _ ->
            dialog.dismiss()
        }

        return dialogBuilder.create()
    }
}