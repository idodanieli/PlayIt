package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.ChessView

interface GameEvent

class MoveEvent(val movedPiece: Piece, val move: Move) : GameEvent
class AbilityActivatedEvent(val piece: Piece) : GameEvent
class PieceCapturedEvent(val capturedPiece: Piece) : GameEvent

class GameSelectedEvent(val chessView: ChessView, val gameID: String) : GameEvent
class GameStartedEvent(val chessView: ChessView) : GameEvent
class GameOverEvent(val winner: Player) : GameEvent