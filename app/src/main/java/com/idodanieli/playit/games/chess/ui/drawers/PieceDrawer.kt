package com.idodanieli.playit.games.chess.ui.drawers

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.MODE_LOCAL
import com.idodanieli.playit.games.chess.logic.BoardDimensions
import com.idodanieli.playit.games.chess.logic.DEFAULT_DIMENSIONS
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.King
import com.idodanieli.playit.games.chess.variants.Game

open class PieceDrawer (
    context: Context,
    var mode: String,
    dimensions: BoardDimensions = DEFAULT_DIMENSIONS,
    var hero: Player = Player.WHITE
): Drawer(dimensions) {
    
    companion object {
        private var BITMAPS: MutableMap<Player, MutableMap<String, Bitmap>> = mutableMapOf()
        private var PICTURES: MutableMap<Player, MutableMap<String, Int>> = mutableMapOf(
            Player.WHITE to mutableMapOf(),
            Player.BLACK to mutableMapOf()
        )

        fun addPiecePicture(type: String, player: Player, picture: Int) {
            PICTURES[player]!![type] = picture
        }

        private fun loadBitmaps(resources: Resources) {
            if (BITMAPS.isNotEmpty()) { return }

            Drawer.loadBitmaps(resources)

            BITMAPS[Player.WHITE] = mutableMapOf()
            BITMAPS[Player.BLACK] = mutableMapOf()

            for ((player, pictures) in PICTURES) {
                for ((type, picture) in pictures) {
                    BITMAPS[player]!![type] = BitmapFactory.decodeResource(resources, picture)
                }
            }
        }

        fun getPieceBitmap(piece: Piece): Bitmap? {
            return BITMAPS[piece.player]?.get(piece.type)
        }
    }

    init {
        loadBitmaps(context.resources)
    }

    fun drawPiece(piece: Piece) {
        val pieceBitmap = getPieceBitmapAccordingToHero(piece)

        if (hero.isBlack()) {
            drawBitmapAtSquare(piece.square.flipVertically(dimensions.rows), pieceBitmap)
            return
        }

        drawBitmapAtSquare(piece.square, pieceBitmap)
    }

    fun drawPieceAtRect(piece: Piece, rect: RectF) {
        val pieceBitmap = getPieceBitmapAccordingToHero(piece)
        drawBitmapAtRect(pieceBitmap, rect)
    }

    private fun getPieceBitmapAccordingToHero(piece: Piece): Bitmap {
        var pieceBitmap = getPieceBitmap(piece)!!

        // Flip the black players piece in local mode so it would be easier to play
        if (mode == MODE_LOCAL && piece.player.isBlack()) {
            pieceBitmap = flipBitmap(pieceBitmap, Direction.VERTICAL)!!
        }

        return pieceBitmap
    }
}