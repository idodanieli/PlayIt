package com.idodanieli.playit.games.chess.ui

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.idodanieli.playit.games.chess.MODE_LOCAL
import com.idodanieli.playit.games.chess.variants.Game
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece

private const val MOVING_PIECE_SCALE = 1.5f
private const val MOVING_PIECE_Y_OFFSET = 150f // so the user will see what piece hes moving

class ChessDrawer(private val size: Int, var mode: String, context: Context) : Drawer() {
    companion object {
        var BITMAPS: MutableMap<Player, MutableMap<String, Bitmap>> = mutableMapOf()
        var PICTURES: MutableMap<Player, MutableMap<String, Int>> = mutableMapOf(
            Player.WHITE to mutableMapOf(),
            Player.BLACK to mutableMapOf()
        )

        fun addPiecePicture(type: String, player: Player, picture: Int) {
            PICTURES[player]!![type] = picture
        }

        private fun loadBitmaps(resources: Resources) {
            if (BITMAPS.isNotEmpty()) { return }

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

    private var squareSize = 0f

    private val lightColor = fetchColorFromAttribute(context, androidx.appcompat.R.attr.colorAccent)
    private val darkColor = fetchColorFromAttribute(context, androidx.appcompat.R.attr.colorPrimaryDark)

    var hero = Player.WHITE

    init {
        loadBitmaps(context.resources)
    }

    fun setSize(size: Float) {
        this.squareSize = size
    }

    // drawChessboard.board draws the whole chessboard ( without the pieces )
    fun drawChessboard() {
        val chessboardSquares = getChessboardSquares()
        drawSquares(chessboardSquares, lightColor, darkColor)
    }

    // @movingPiece: the piece currently touched by the user. will be drawn on the touched position and not at any specific square
    fun drawPieces(game: Game, movingPiece: MovingPiece?) {
        game.pieces().forEach { piece ->
            if (movingPiece == null || piece != movingPiece.piece) {
                this.drawPiece(piece)
            }
        }

        movingPiece?.let {
            this.drawBitmapAtPosition(
                it.x,
                it.y - MOVING_PIECE_Y_OFFSET,
                it.bitmap,
                scale = MOVING_PIECE_SCALE
            )
        }
    }

    fun drawPiece(piece: Piece) {
        var pieceBitmap = getPieceBitmap(piece)!!

        // Flip the black players piece in local mode so it would be easier to play
        if (mode == MODE_LOCAL && piece.player.isBlack()) {
            pieceBitmap = flipBitmap(pieceBitmap, Direction.VERTICAL)!!
        }

        if (hero.isBlack()) {
            drawBitmapAtSquare(piece.square.flipVertically(size), pieceBitmap)
            return
        }

        drawBitmapAtSquare(piece.square, pieceBitmap)
    }

    private fun getChessboardSquares(): List<Square> {
        return (0..size).flatMap { col ->
            (0..size).map { row ->
                Square(col, row)
            }
        }
    }

    fun drawSquares(squares: List<Square>, lightColor: Int, darkColor: Int) {
        for (square in squares) {
            drawSquareAccordingToHero(square, lightColor, darkColor)
        }
    }

    private fun drawSquareAccordingToHero(square: Square, lightColor: Int, darkColor: Int) {
        val color = getSquareColor(square, lightColor, darkColor)
        drawSquareAccordingToHero(square, color)
    }

    fun drawSquareAccordingToHero(square: Square, color: Int) {
        // When the player is black the screen is flipped vertically so it's fitting to his perspective
        val squareAccordingToHero = if(hero.isBlack()) square.flipVertically(size) else square

        drawSquare(squareAccordingToHero, color)
    }

    private fun getSquareColor(square: Square, lightColor: Int, darkColor: Int): Int {
        if(isDarkSquare(square)) {
            return darkColor
        }

        return lightColor
    }

    private fun isDarkSquare(square: Square): Boolean {
        return (square.col + square.row) % 2 == 1
    }

    // --- Raw Drawing -----------------------------------------------------------------------------
    private fun drawSquare(square: Square, color: Int) {
        this.canvas.drawRect(
            square.col * squareSize,
            this.canvas.height - square.row * squareSize,
            (square.col + 1) * squareSize,
            this.canvas.height - (square.row + 1) * squareSize,
            getPaint(color)
        )
    }

    private fun drawBitmapAtSquare(square: Square, bitmap: Bitmap) {
        val rect = RectF(
            square.col * squareSize,
            (size - 1 - square.row) * squareSize,
            (square.col + 1) * squareSize,
            ((size - 1 - square.row) + 1) * squareSize
        )

        drawBitmapAtRect(bitmap, rect)
    }

    // @param scale = the scaling of the piece
    private fun drawBitmapAtPosition(x: Float, y: Float, bitmap: Bitmap, scale: Float = 1f) {
        val rect = RectF(
            x - squareSize * scale / 2,
            y - squareSize * scale / 2,
            x + squareSize * scale / 2,
            y + squareSize * scale / 2
        )

        drawBitmapAtRect(bitmap, rect)
    }

    fun drawBitmapAtRect(bitmap: Bitmap, rect: RectF) {
        canvas.drawBitmap(bitmap, null, rect, Paint())
    }
}
