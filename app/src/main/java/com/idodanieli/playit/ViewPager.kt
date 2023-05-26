package com.idodanieli.playit

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.idodanieli.playit.games.chess.ui.ChessView

fun ViewPager2.currentPage(): View {
    return getChildAt(currentItem)
}

fun ViewPager2.currentChessview(): ChessView {
    return currentPage().findViewById(R.id.chess_view)
}