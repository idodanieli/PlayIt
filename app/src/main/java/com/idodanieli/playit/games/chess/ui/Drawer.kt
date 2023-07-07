package com.idodanieli.playit.games.chess.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.TypedValue
import com.idodanieli.playit.games.chess.logic.Square


open class Drawer (var rowCount: Int) {

    var squareSize = 0f
    var canvas = Canvas()

    fun initialize(canvas: Canvas, squareSize: Float) {
        this.canvas = canvas
        this.squareSize = squareSize
    }

    fun drawSquare(square: Square, color: Int) {
        this.canvas.drawRect(
            square.col * squareSize,
            this.canvas.height - square.row * squareSize,
            (square.col + 1) * squareSize,
            this.canvas.height - (square.row + 1) * squareSize,
            getPaint(color)
        )
    }

    fun drawBitmapAtSquare(square: Square, bitmap: Bitmap) {
        val rect = RectF(
            square.col * squareSize,
            (rowCount - 1 - square.row) * squareSize,
            (square.col + 1) * squareSize,
            ((rowCount - 1 - square.row) + 1) * squareSize
        )

        drawBitmapAtRect(bitmap, rect)
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