package com.idodanieli.playit.games.chess.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.ui.dialogs.GameOverDialog

class DialogBuilder(val context: Context, private val supportFragmentManager: FragmentManager) {

    fun showGameOverDialog(winner: Player?, hero: Player, onPositiveButton: Runnable) {
        GameOverDialog(winner, hero, onPositiveButton).show(supportFragmentManager, "GameOverDialog")
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