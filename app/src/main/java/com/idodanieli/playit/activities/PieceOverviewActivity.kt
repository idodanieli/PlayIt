package com.idodanieli.playit.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.logic.GameParser
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.Common.Companion.setDimensions
import com.idodanieli.playit.games.chess.variants.ClassicGame

class PieceOverviewActivity: AppCompatActivity() {
    companion object {
        const val DISPLAY_CHESSBOARD_SIZE = 7
        val DISPLAY_SQUARE = Square(3, 3)
    }

    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var chessView: ChessView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_overview)
        initUI()

        val piece = getPiece()
        showPiece(piece)
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
    private fun getPiece(): Piece {
        val type = intent.getStringExtra("type")

        return GameParser.pieceFromCharacter(type!!, DISPLAY_SQUARE, Player.WHITE)
    }

    private fun showPiece(piece: Piece) {
        // title.text = piece.name
        // description.text = piece.description
        piece.square = DISPLAY_SQUARE
        chessView.game = ClassicGame("", setOf(piece), CHESSBOARD_SIZE - 1)

        chessView.onTouchReleased(DISPLAY_SQUARE)
        chessView.invalidate()
    }
}