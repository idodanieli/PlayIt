package com.idodanieli.playit.games.chess.ui

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.MODE_LOCAL
import com.idodanieli.playit.games.chess.logic.Game
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.pieces.classic.*
import com.idodanieli.playit.games.chess.pieces.fairy.*


var BITMAPS: MutableMap<Player, MutableMap<String, Bitmap>> = mutableMapOf()

val COLOR_TOUCHED = Color.parseColor("#CBC3E3")
val COLOR_LIGHT_AVAILABLE_SQUARE = Color.parseColor("#FF7276")
val COLOR_DARK_AVAILABLE_SQUARE = Color.parseColor("#E6676B")

private const val MOVING_PIECE_SCALE = 1.5f
private const val MOVING_PIECE_Y_OFFSET = 150f // so the user will see what piece hes moving

class ChessDrawer(private val size: Int, var mode: String, context: Context) : Drawer() {
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

    // drawAvailableMoves draw the moves available by a piece to move to in a red color
    fun drawAvailableMoves(moves: Set<Move>) {
        return drawAvailableSquares(moves.map { it.dest })
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

    fun drawTouchedSquare(square: Square) {
        drawSquareAccordingToHero(square, COLOR_TOUCHED)
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

    private fun drawAvailableSquares(squares: List<Square>) {
        drawSquares(squares, COLOR_LIGHT_AVAILABLE_SQUARE, COLOR_DARK_AVAILABLE_SQUARE)
    }

    private fun drawSquares(squares: List<Square>, lightColor: Int, darkColor: Int) {
        for (square in squares) {
            drawSquareAccordingToHero(square, lightColor, darkColor)
        }
    }

    private fun drawSquareAccordingToHero(square: Square, lightColor: Int, darkColor: Int) {
        val color = getSquareColor(square, lightColor, darkColor)
        drawSquareAccordingToHero(square, color)
    }

    private fun drawSquareAccordingToHero(square: Square, color: Int) {
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

// --- BITMAPS -------------------------------------------------------------------------------------
fun getPieceBitmap(piece: Piece): Bitmap? {
    return BITMAPS[piece.player]?.get(piece.type)
}

// TODO: add this in a creator in the pieces class
private fun loadBitmaps(resources: Resources) {
    if (BITMAPS.isNotEmpty()) { return }

    BITMAPS[Player.WHITE] = mutableMapOf(
        TYPE_KING to BitmapFactory.decodeResource(resources, R.drawable.king_white),
        TYPE_QUEEN to BitmapFactory.decodeResource(resources, R.drawable.queen_white),
        TYPE_ROOK to BitmapFactory.decodeResource(resources, R.drawable.rook_white),
        TYPE_BISHOP to BitmapFactory.decodeResource(resources, R.drawable.bishop_white),
        TYPE_KNIGHT to BitmapFactory.decodeResource(resources, R.drawable.knight_white),
        TYPE_PAWN to BitmapFactory.decodeResource(resources, R.drawable.pawn_white),
        TYPE_VENOM to BitmapFactory.decodeResource(resources, R.drawable.venom_white),
        TYPE_BEROLINA_PAWN to BitmapFactory.decodeResource(resources, R.drawable.berolina_white),
        TYPE_GIRAFFE to BitmapFactory.decodeResource(resources, R.drawable.giraffe_white),
        TYPE_ZEBRA to BitmapFactory.decodeResource(resources, R.drawable.zebra_white),
        TYPE_CENTAUR to BitmapFactory.decodeResource(resources, R.drawable.centaur_white),
        TYPE_ELEPHANT to BitmapFactory.decodeResource(resources, R.drawable.elephant_white),
        TYPE_GRASSHOPPER to BitmapFactory.decodeResource(resources, R.drawable.grasshopper_white),
        TYPE_CAMEL to BitmapFactory.decodeResource(resources, R.drawable.camel_white),
        TYPE_WILDBEAST to BitmapFactory.decodeResource(resources, R.drawable.wildbeast_white),
        TYPE_AMAZON to BitmapFactory.decodeResource(resources, R.drawable.amazon_white),
        TYPE_EMPRESS to BitmapFactory.decodeResource(resources, R.drawable.empress_white),
        TYPE_ARCHBISHOP to BitmapFactory.decodeResource(resources, R.drawable.archbishop_white),
        TYPE_XIANGQI_HORSE to BitmapFactory.decodeResource(resources, R.drawable.xiangqi_horse_white),
        Jester.TYPE to BitmapFactory.decodeResource(resources, R.drawable.jester_white),
        Terrorist.TYPE to BitmapFactory.decodeResource(resources, R.drawable.terrorist_white)
    )

    BITMAPS[Player.BLACK] = mutableMapOf(
        TYPE_KING to BitmapFactory.decodeResource(resources, R.drawable.king_black),
        TYPE_QUEEN to BitmapFactory.decodeResource(resources, R.drawable.queen_black),
        TYPE_ROOK to BitmapFactory.decodeResource(resources, R.drawable.rook_black),
        TYPE_BISHOP to BitmapFactory.decodeResource(resources, R.drawable.bishop_black),
        TYPE_KNIGHT to BitmapFactory.decodeResource(resources, R.drawable.knight_black),
        TYPE_PAWN to BitmapFactory.decodeResource(resources, R.drawable.pawn_black),
        TYPE_VENOM to BitmapFactory.decodeResource(resources, R.drawable.venom_black),
        TYPE_BEROLINA_PAWN to BitmapFactory.decodeResource(resources, R.drawable.berolina_black),
        TYPE_GIRAFFE to BitmapFactory.decodeResource(resources, R.drawable.giraffe_black),
        TYPE_ZEBRA to BitmapFactory.decodeResource(resources, R.drawable.zebra_black),
        TYPE_CENTAUR to BitmapFactory.decodeResource(resources, R.drawable.centaur_black),
        TYPE_ELEPHANT to BitmapFactory.decodeResource(resources, R.drawable.elephant_black),
        TYPE_GRASSHOPPER to BitmapFactory.decodeResource(resources, R.drawable.grasshopper_black),
        TYPE_CAMEL to BitmapFactory.decodeResource(resources, R.drawable.camel_black),
        TYPE_WILDBEAST to BitmapFactory.decodeResource(resources, R.drawable.wildbeast_black),
        TYPE_AMAZON to BitmapFactory.decodeResource(resources, R.drawable.amazon_black),
        TYPE_EMPRESS to BitmapFactory.decodeResource(resources, R.drawable.empress_black),
        TYPE_ARCHBISHOP to BitmapFactory.decodeResource(resources, R.drawable.archbishop_black),
        TYPE_XIANGQI_HORSE to BitmapFactory.decodeResource(resources, R.drawable.xiangqi_horse_black),
        Jester.TYPE to BitmapFactory.decodeResource(resources, R.drawable.jester_black),
        Terrorist.TYPE to BitmapFactory.decodeResource(resources, R.drawable.terrorist_black)
    )
}