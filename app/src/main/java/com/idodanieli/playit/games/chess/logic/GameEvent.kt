package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.ChessView

interface GameEvent

class MoveEvent(val move: Move): GameEvent
class PieceCapturedEvent(val capturedPiece: Piece): GameEvent

class GameStartedEvent(val chessView: ChessView): GameEvent
class GameOverEvent(val winner: Player): GameEvent