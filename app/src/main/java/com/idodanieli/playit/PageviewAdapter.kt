package com.idodanieli.playit

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.idodanieli.playit.games.chess.logic.Game
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.GameListener


class PageviewAdapter(private val mList: List<Game>) :
    RecyclerView.Adapter<PageviewAdapter.ViewHolder>() {

    private lateinit var context: Context

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(context)
            .inflate(R.layout.chessview_container, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = mList[position]

        holder.gameTitle.text = game.name
        holder.gameDescription.text = game.description
        holder.chessView.game = game
        holder.chessView.setGameListener(object : GameListener {
            override fun onGameOver() {
                context.showDialog("${game.currentPlayer} Won!")
            }
        })
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val chessView: ChessView = itemView.findViewById(R.id.chess_view)
        val gameTitle: TextView = itemView.findViewById(R.id.gameName)
        val gameDescription: TextView = itemView.findViewById(R.id.gameDescription)
    }
}

fun Context.showDialog(winner: String) {
    val dialogBuilder = AlertDialog.Builder(this)

    // Set dialog title and message
    dialogBuilder.setTitle("GAME OVER")
    dialogBuilder.setMessage(winner)

    // Set positive button with click listener
    dialogBuilder.setPositiveButton("NEW GAME") { dialog, _ ->
        // Handle positive button click
        dialog.dismiss()
    }

    // Set negative button with click listener
    dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
        // Handle negative button click
        dialog.dismiss()
    }

    // Create and show the dialog
    val dialog = dialogBuilder.create()
    dialog.show()
}