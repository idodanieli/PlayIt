package com.idodanieli.playit

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.idodanieli.playit.games.chess.logic.Game
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.game_listener.ChessGameListener
import com.idodanieli.playit.games.chess.game_listener.GameListener


class PageviewAdapter(
    private val games: List<Game>,
    private val gameListener: GameListener
    ) :
    RecyclerView.Adapter<PageviewAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chessview_container, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]

        holder.gameTitle.text = game.name
        holder.gameDescription.text = game.description
        holder.chessView.game = game
        holder.chessView.setGameListener(gameListener)

        holder.chessView.heroTextView = holder.playerHero
        holder.chessView.heroTextView.text = SharedPrefsManager.getInstance().getUsername()

        holder.chessView.opponentTextView = holder.playerOpponent
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return games.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val chessView: ChessView = itemView.findViewById(R.id.chess_view)
        val gameTitle: TextView = itemView.findViewById(R.id.gameName)
        val gameDescription: TextView = itemView.findViewById(R.id.gameDescription)

        val playerHero: TextView = itemView.findViewById(R.id.playerHero)
        val playerOpponent: TextView = itemView.findViewById(R.id.playerOpponent)
    }
}