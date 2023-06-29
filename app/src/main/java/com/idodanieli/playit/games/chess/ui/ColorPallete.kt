package com.idodanieli.playit.games.chess.ui

import android.graphics.Color

class ColorPallete {
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
}