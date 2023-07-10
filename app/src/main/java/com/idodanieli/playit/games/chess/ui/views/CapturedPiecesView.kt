package com.idodanieli.playit.games.chess.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.idodanieli.playit.games.chess.MODE_DEFAULT
import com.idodanieli.playit.games.chess.game_subscriber.GameEvent
import com.idodanieli.playit.games.chess.game_subscriber.GameSubscriber
import com.idodanieli.playit.games.chess.game_subscriber.PieceCapturedEvent
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.drawers.PieceDrawer


class CapturedPiecesView(context: Context?, attrs: AttributeSet?): View(context, attrs), GameSubscriber {
    lateinit var player: Player

    private val pieceDrawer = PieceDrawer(context!!, MODE_DEFAULT)
    private var capturedPieces = mutableListOf<Piece>()
    private var capturedPieceSize: Float = 0f

    private fun initialize(canvas: Canvas) {
        this.capturedPieceSize = canvas.height.toFloat()
        this.pieceDrawer.initialize(canvas, capturedPieceSize)
    }

    // --- Logic -----------------------------------------------------------------------------------
    override fun onGameEvent(event: GameEvent) {
        when (event) {
            is PieceCapturedEvent -> {
                if (ourPlayerCaptured(event)) append(event.capturedPiece)
            }
        }
    }

    private fun ourPlayerCaptured(event: PieceCapturedEvent): Boolean {
        return event.capturingPiece.player == player
    }

    private fun append(piece: Piece) {
        capturedPieces += piece
        invalidate()
    }

    fun clear() {
        capturedPieces = mutableListOf()
    }

    // --- UI --------------------------------------------------------------------------------------
    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        this.initialize(canvas)
        this.drawCapturedPieces()
    }

    private fun drawCapturedPieces() {
        for (index in capturedPieces.indices) {
            val piece = capturedPieces[index]
            drawCapturedPiece(piece, index)
        }
    }

    private fun drawCapturedPiece(piece: Piece, index: Int) {
        val bitmap = PieceDrawer.getPieceBitmap(piece) ?: return

        val startX = capturedPieceSize * index
        val endX = startX + capturedPieceSize

        val rect = RectF(startX, 0f, endX, capturedPieceSize)

        pieceDrawer.drawBitmapAtRect(bitmap, rect)
    }
}