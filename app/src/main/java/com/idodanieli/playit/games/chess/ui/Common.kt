package com.idodanieli.playit.games.chess.ui

import android.view.View
import android.view.ViewGroup

class Common {
    companion object {
        fun setDimensions(view: View, width: Int, height: Int) {
            val params: ViewGroup.LayoutParams = view.layoutParams
            params.width = width
            params.height = height
            view.layoutParams = params

            view.requestLayout()
        }
    }
}