package com.idodanieli.playit.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.BoardDimensions
import com.idodanieli.playit.games.chess.logic.GameParser
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.views.ChessView
import com.idodanieli.playit.games.chess.ui.utils.Common.Companion.setDimensions
import com.idodanieli.playit.games.chess.variants.ClassicGame

class PiecePreviewActivity: AppCompatActivity() {
    companion object {
        private val DISPLAY_DIMENSIONS = BoardDimensions(7, 7)
        private val DISPLAY_SQUARE = Square(3, 3)
    }

    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var chessView: ChessView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_piece_preview)
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
        val margin = resources.getDimensionPixelSize(R.dimen.chessview_overview_margin)
        val squareSize = screenWidth - (2 * margin)

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
        chessView.setGame( ClassicGame("", setOf(piece), DISPLAY_DIMENSIONS) )

        chessView.onTouchReleased(DISPLAY_SQUARE)
        chessView.invalidate()
    }
}