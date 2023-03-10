package com.idodanieli.playit

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.idodanieli.playit.games.chess.ChessView
import com.idodanieli.playit.games.chess.Game


class PageviewAdapter(private val mList: List<Game>) :
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
        val game = mList[position]

        holder.gameTitle.text = game.name
        holder.gameDescription.text = game.description
        holder.chessView.game = game
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