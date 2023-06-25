package com.idodanieli.playit.games.chess.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.TypedValue


open class Drawer {

    companion object {
        // --- Available Squares Colors
        val COLOR_LIGHT_AVAILABLE_SQUARE = Color.parseColor("#FF7276")
        val COLOR_DARK_AVAILABLE_SQUARE = Color.parseColor("#E6676B")

        // --- Touched Square Colors
        val COLOR_TOUCHED = Color.parseColor("#CBC3E3") // One Touch - Light Purple
        val COLOR_ABILITY = Color.parseColor("#FFD700") // Two Touches - Gold

        // --- Bomb colors
        val COLOR_SINOPIA_LIGHT = Color.parseColor("#D73F0F")
        val COLOR_SINOPIA_DARK = Color.parseColor("#D70500")
    }

    var canvas = Canvas()

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