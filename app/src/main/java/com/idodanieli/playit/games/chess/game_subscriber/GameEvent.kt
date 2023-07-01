package com.idodanieli.playit.games.chess.game_subscriber

import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.ChessView

interface GameEvent

class MoveEvent(val movedPiece: Piece, val move: Move) : GameEvent
class PieceCapturedEvent(val capturedPiece: Piece) : GameEvent

class GameSelectedEvent(val chessView: ChessView, val gameID: String) : GameEvent
class PlayersJoinedEvent(val hero: String, val opponent: String) : GameEvent
class GameStartedEvent(val chessView: ChessView) : GameEvent
class GameOverEvent(val winner: Player?) : GameEvent