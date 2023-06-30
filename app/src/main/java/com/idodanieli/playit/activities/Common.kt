package com.idodanieli.playit.activities

import android.content.Context
import android.content.Intent
import com.idodanieli.playit.games.chess.pieces.Piece

fun Context.openRegisterActivity() {
    val intent = Intent(this, RegisterActivity::class.java)
    startActivity(intent)
}

fun Context.openPiecePreviewActivity(piece: Piece) {
    val intent = Intent(this, PiecePreviewActivity::class.java)
    intent.putExtra("type", piece.type)
    this.startActivity(intent)
}