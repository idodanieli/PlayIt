package com.idodanieli.playit

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.idodanieli.playit.games.chess.ui.views.CapturedPiecesView
import com.idodanieli.playit.games.chess.ui.views.ChessView
import com.idodanieli.playit.games.chess.ui.views.PlayerView
import com.idodanieli.playit.games.chess.ui.views.TimerView

fun ViewPager2.currentPage(): View {
    return getChildAt(0)
}

// --- Get views within a page ---------------------------------------------------------------------
fun ViewPager2.currentChessview(): ChessView {
    return currentPage().findViewById(R.id.chess_view)
}

fun ViewPager2.currentHeroPlayerView(): PlayerView {
    return currentPage().findViewById(R.id.heroPlayerView)
}

fun ViewPager2.currentOpponentPlayerView(): PlayerView {
    return currentPage().findViewById(R.id.opponentPlayerView)
}

// --- Logic ---------------------------------------------------------------------------------------
fun ViewPager2.gameIsPlaying(): Boolean {
    return currentChessview().game.started
}