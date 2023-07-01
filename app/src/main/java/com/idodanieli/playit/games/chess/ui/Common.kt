package com.idodanieli.playit.games.chess.ui

import android.view.View
import android.view.ViewGroup
import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.variants.Game

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