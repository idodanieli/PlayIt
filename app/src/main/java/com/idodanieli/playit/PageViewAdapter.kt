package com.idodanieli.playit

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.idodanieli.playit.games.chess.ui.utils.Common.Companion.setDimensions
import com.idodanieli.playit.games.chess.ui.views.CapturedPiecesView
import com.idodanieli.playit.games.chess.ui.views.ChessView
import com.idodanieli.playit.games.chess.ui.views.PlayerView
import com.idodanieli.playit.games.chess.ui.views.TimerView
import com.idodanieli.playit.games.chess.variants.Game


class PageViewAdapter(
    private val games: List<Game>,
    private val screenWidth: Int,
    private val context: Context,
    ) :
    RecyclerView.Adapter<PageViewAdapter.ViewHolder>() {

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        private val chessView: ChessView = itemView.findViewById(R.id.chess_view)
        private val gameTitle: TextView = itemView.findViewById(R.id.gameName)
        private val gameDescription: TextView = itemView.findViewById(R.id.gameDescription)

        private val heroPlayerView: PlayerView = itemView.findViewById(R.id.heroPlayerView)
        private val opponentPlayerView: PlayerView = itemView.findViewById(R.id.opponentPlayerView)

        fun clear() {
            chessView.clear()
            heroPlayerView.clear()
            opponentPlayerView.clear()
        }

        fun setupChessView(game: Game, width: Int, length: Int) {
            chessView.setGame(game)

            this.subscribeComponentsToChessView()
            setDimensions(chessView, width, length)
        }

        private fun subscribeComponentsToChessView() {
            chessView.subscribe(heroPlayerView)
            chessView.subscribe(opponentPlayerView)

            chessView.subscribeVisualizers()
        }

        fun setGameInfo(game: Game) {
            gameTitle.text = game.name
            gameDescription.text = game.description
        }
    }

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

        // Clears the state of the ViewHolder
        // This class is reusable and without clearing it causes bugs
        holder.clear()

        holder.setGameInfo(game)
        holder.setupChessView(game, screenWidth, screenWidth)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return games.size
    }
}