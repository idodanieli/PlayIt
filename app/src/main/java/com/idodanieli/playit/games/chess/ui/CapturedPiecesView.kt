package com.idodanieli.playit.games.chess.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.MODE_DEFAULT
import com.idodanieli.playit.games.chess.pieces.Piece


class CapturedPiecesView(context: Context?, attrs: AttributeSet?): View(context, attrs) {

    private val chessDrawer = ChessDrawer(CHESSBOARD_SIZE, MODE_DEFAULT, context!!)
    private var capturedPieceSize = height.toFloat()

    private var capturedPieces = mutableListOf<Piece>()

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        capturedPieceSize = height.toFloat()
        chessDrawer.canvas = canvas
        chessDrawer.squareSize = capturedPieceSize

        drawCapturedPieces()
    }

    fun append(piece: Piece) {
        capturedPieces += piece
        invalidate()
    }

    private fun drawCapturedPieces() {
        for (index in capturedPieces.indices) {
            val piece = capturedPieces[index]
            drawCapturedPiece(piece, index)
        }
    }

    private fun drawCapturedPiece(piece: Piece, index: Int) {
        val bitmap = ChessDrawer.getPieceBitmap(piece) ?: return

        val startX = capturedPieceSize * index
        val endX = startX + capturedPieceSize

        val rect = RectF(startX, 0f, endX, capturedPieceSize)

        chessDrawer.drawBitmapAtRect(bitmap, rect)
    }
}