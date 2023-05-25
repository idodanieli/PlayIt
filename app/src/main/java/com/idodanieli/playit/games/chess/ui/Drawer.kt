package com.idodanieli.playit.games.chess.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint


open class Drawer {
    var canvas = Canvas()

    /**
     * Creates a new bitmap by flipping the specified bitmap vertically or horizontally.
     * @param src        Bitmap to flip
     * @param type       Flip direction (horizontal or vertical)
     * @return           New bitmap created by flipping the given on vertically or horizontally as
     * specified by the `type` parameter or the original bitmap if an unknown type is specified.
     */
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


// getPaint returns a paint with the given color
fun getPaint(color: Int): Paint {
    val paint = Paint()
    paint.color = color

    return paint
}