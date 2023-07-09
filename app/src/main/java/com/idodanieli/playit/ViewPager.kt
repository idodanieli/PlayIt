package com.idodanieli.playit

import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.idodanieli.playit.games.chess.ui.CapturedPiecesView
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.ui.TimerView

fun ViewPager2.currentPage(): View {
    return getChildAt(0)
}

// --- Get views within a page ---------------------------------------------------------------------
fun ViewPager2.currentChessview(): ChessView {
    return currentPage().findViewById(R.id.chess_view)
}

fun ViewPager2.currentHeroCapturedPiecesView(): CapturedPiecesView {
    return currentPage().findViewById(R.id.herosCapturedPieces)
}
fun ViewPager2.currentOpponentCapturedPiecesView(): CapturedPiecesView {
    return currentPage().findViewById(R.id.opponentsCapturedPieces)
}

fun ViewPager2.currentHeroTimerView(): TimerView {
    return currentPage().findViewById(R.id.heroTimer)
}
fun ViewPager2.currentOpponentTimerView(): TimerView {
    return currentPage().findViewById(R.id.opponentTimer)
}

// --- Logic ---------------------------------------------------------------------------------------
fun ViewPager2.gameIsPlaying(): Boolean {
    return currentChessview().game.started
}