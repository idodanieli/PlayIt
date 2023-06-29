package com.idodanieli.playit.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.idodanieli.playit.MainActivity
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.Common.Companion.setDimensions
import com.idodanieli.playit.games.chess.variants.Game

class GameOverviewActivity: AppCompatActivity() {
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var chessView: ChessView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_overview)
        initUI()

        val game = getGame()
        showGame(game)
    }

    // --- UI ------------------------------------------------------------------
    private fun initUI() {
        findViewsByIds()
        setChessViewSize()
    }

    private fun findViewsByIds() {
        title = findViewById(R.id.gameName)
        description = findViewById(R.id.gameDescription)
        chessView = findViewById(R.id.chessview)
    }

    private fun setChessViewSize() {
        val screenWidth = resources.displayMetrics.widthPixels
        val squareSize = screenWidth - (2 * resources.getDimensionPixelSize(R.dimen.chessview_overview_margin))

        setDimensions(chessView, squareSize, squareSize)
    }

    // --- GENERAL -----------------------------------------------------------------
    private fun getGame(): Game {
        val idx = intent.getIntExtra("game_index", 0)

        return MainActivity.games[idx]
    }

    private fun showGame(game: Game) {
        title.text = game.name
        description.text = game.description
        chessView.game = game
    }
}