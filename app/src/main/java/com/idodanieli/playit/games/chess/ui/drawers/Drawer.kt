package com.idodanieli.playit.games.chess.ui.drawers

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.*
import android.util.TypedValue
import com.idodanieli.playit.R
import com.idodanieli.playit.games.chess.logic.BoardDimensions
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import java.sql.Ref


open class Drawer (protected val dimensions: BoardDimensions) {
    companion object {
        lateinit var RED_GLOW_INDICATOR: Bitmap

        fun loadBitmaps(resources: Resources) {
            RED_GLOW_INDICATOR = BitmapFactory.decodeResource(resources, R.drawable.indicator_red_glow)
        }
    }

    var squareSize = 0f
    var canvas = Canvas()

    open fun initialize(canvas: Canvas, squareSize: Float) {
        this.canvas = canvas
        this.squareSize = squareSize
    }

    fun drawSquare(square: Square, color: Int) {
        this.canvas.drawRect(
            convertSquareToRectF(square),
            getPaint(color)
        )
    }

    fun drawBitmapAtSquare(square: Square, bitmap: Bitmap) {
        drawBitmapAtRect(bitmap, convertSquareToRectF(square))
    }

    private fun convertSquareToRectF(square: Square): RectF {
        return RectF(
            square.col * squareSize,
            (dimensions.rows - 1 - square.row) * squareSize,
            (square.col + 1) * squareSize,
            ((dimensions.rows - 1 - square.row) + 1) * squareSize
        )
    }

    fun convertSquareToRectFAccordingToHero(square: Square, hero: Player): RectF {
        if (hero.isBlack()) {
            return convertSquareToRectF(square.flipVertically(dimensions.rows))
        }

        return convertSquareToRectF(square)
    }

    fun drawBitmapAtRect(bitmap: Bitmap, rect: RectF) {
        canvas.drawBitmap(bitmap, null, rect, Paint())
    }

    fun flipBitmap(src: Bitmap, type: Direction): Bitmap? {
        val matrix = Matrix()
        if (type == Direction.VERTICAL) {
            matrix.preScale(1.0f, -1.0f)
        } else if (type == Direction.HORIZONTAL) {
            matrix.preScale(-1.0f, 1.0f)
        } else {
            return src
        }
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }
}

enum class Direction {
    VERTICAL, HORIZONTAL
}

// fetchColorFromAttribute in the theme
fun fetchColorFromAttribute(context: Context, attribute: Int): Int {
    val typedValue = TypedValue()
    val a: TypedArray = context.obtainStyledAttributes(typedValue.data, intArrayOf(attribute))

    val color = a.getColor(0, 0)
    a.recycle()

    return color
}

// getPaint returns a paint with the given color
fun getPaint(color: Int): Paint {
    val paint = Paint()
    paint.color = color

    return paint
}