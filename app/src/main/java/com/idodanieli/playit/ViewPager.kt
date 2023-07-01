package com.idodanieli.playit

import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.idodanieli.playit.games.chess.ui.CapturedPiecesView
import com.idodanieli.playit.games.chess.ui.ChessView

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

// --- Set players names when they join a game ----------------------------------------------------
fun ViewPager2.setPlayers(hero: String, opponent: String) {
    setPlayer(currentPage().findViewById(R.id.playerHero), hero)
    setPlayer(currentPage().findViewById(R.id.playerOpponent), opponent)
}

private fun setPlayer(textView: TextView, player: String) {
    textView.text = player
    textView.visibility = View.VISIBLE
}