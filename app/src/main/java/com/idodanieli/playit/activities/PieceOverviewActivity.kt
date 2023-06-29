package com.idodanieli.playit.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.idodanieli.playit.MainActivity
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.classic.Queen
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.Common.Companion.setDimensions
import com.idodanieli.playit.games.chess.variants.ClassicGame
import com.idodanieli.playit.games.chess.variants.Game

class PieceOverviewActivity: AppCompatActivity() {
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var chessView: ChessView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_overview)
        initUI()

        val game = getGame()
        // showGame(game)

        showDummyGame()
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

    private fun showDummyGame() {
        val overviewedSquare = Square(3, 3)
        val overviewedPiece = Queen(overviewedSquare, Player.WHITE)
        val game = ClassicGame("Dummy Game", setOf(overviewedPiece), CHESSBOARD_SIZE - 1)
        chessView.game = game

        chessView.onTouchReleased(overviewedSquare)
        chessView.invalidate()
    }
}