package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.pieces.Piece

interface GameEvent

class PieceCapturedEvent(val capturedPiece: Piece): GameEvent
class GameOverEvent(val winner: Player): GameEvent