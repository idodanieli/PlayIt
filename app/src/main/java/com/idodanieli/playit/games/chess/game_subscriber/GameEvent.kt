package com.idodanieli.playit.games.chess.game_subscriber

import com.idodanieli.playit.games.chess.logic.Move
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.pieces.Piece
import com.idodanieli.playit.games.chess.ui.ChessView
import com.idodanieli.playit.games.chess.variants.Game

interface GameEvent

class MoveEvent(val movedPiece: Piece, val move: Move) : GameEvent
class PieceCapturedEvent(val capturedPiece: Piece, val capturingPiece: Piece) : GameEvent
class CheckEvent(val checkedPlayer: Player, val game: Game): GameEvent

class GameSelectedEvent(val chessView: ChessView, val gameID: String) : GameEvent
class PlayersJoinedEvent(val hero: String, val opponent: String) : GameEvent
class GameStartedEvent(val chessView: ChessView) : GameEvent
class GameOverEvent(val winner: Player?, val hero: Player) : GameEvent