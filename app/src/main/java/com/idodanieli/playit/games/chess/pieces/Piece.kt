package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.logic.Board
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.pieces.core.MovementType

const val NO_MAX_STEPS = 0

interface Piece {
    var square: Square
    var player: Player
    var moved: Boolean
    val type: String
    val movementType: MovementType

    fun xrayPossibleMove(board: Board): List<Square>

    // captureMoves returns all the squares the piece can capture in (most of the times it will be like
    // possibleMoves, except for special cases like Pawns, etc.)
    fun captureMoves(board: Board): List<Square>

    // possibleMoves returns all the squares a piece can move to, without taking general logic
    // into consideration like pinning, etc.
    fun possibleMoves(board: Board): List<Square>

    // validMoves returns a list of the squares the piece can move to
    fun validMoves(board: Board, ignoreSamePlayer: Boolean = false): List<Square>

    // onMove adds logic to piece after they have been moved
    fun onMove()

    // onEat adds logic to piece after they have eaten another piece
    fun onEat(eatenPiece: Piece)
}

// https://en.wikipedia.org/wiki/Giraffe_%28chess%29
// https://spuf.org/2018/10/12/my-7-favorite-fairy-chess-pieces/
// https://en.wikipedia.org/wiki/Fairy_chess_piece

/////// PIECES IDEAS \\\\\\\\

// - FAIRY CHESS PIECES - \\

// -- LEAPERS
// V "Girrafe"
// V "Zebra"
// V "Centaur":     A piece that can move as a knight or as a pawn.
// V "Camel"        (3, 1) Knight

// -- HOPPERS
// V "Grasshopper"
// V "Elephant"

// -- COMPOUND
// V "WildBeast":   Knight + Camel
// V "Amazon":      Knight + Queen
// V "Empress":     Knight + Rook

// -- RIDERS
// V "Xiangqi Horse": Knight that cant leap

// -- OTHER
// V "Berolina Pawn"

//----- SPECIAL PIECES -----\\
// - "Jumper":     A piece that can jump over an enemy piece to capture it, similar to a knight.
// - "Splitter":   A piece that can split into two smaller pieces when captured, each of which can move and capture like pawns.
// - "Bomber":     A piece that can be captured like a normal piece, but explodes and capture all surrounding pieces when it is captured.
// - "Slider":     A piece that can move in a straight line any number of squares, but must be blocked by another piece or reach the edge of the board to stop.
// - "Chameleon":  A piece that can change its appearance to look like any other piece on the board.
// - "Ghost":      A piece that can move through other pieces and.
// - "Mimic":      A piece that can change its movement abilities to copy any piece it lands on.
// - "Amoeba":     A piece that can split into two pieces and move separately, rejoining to form the original piece later
// - "Ambusher":    A piece that can move two squares in any direction, but can only attack pieces that are behind it.
// - "Archbishop": A combination of a bishop and a knight.
// - "Chancellor": A combination of a rook and a knight.
// - "Possessor" / "Puppeteer": "The Puppeteer piece is a powerful and unique chess piece that has the ability to control and manipulate enemy pieces by taking control of their movements on the board. It is a strategic piece that can turn the tide of the game by disrupting the opponent's plans and forcing them to play in unexpected ways. With careful planning, the Puppeteer can be used to create new opportunities for your own pieces and ultimately lead to victory"
// - "Trickster": The Trickster chess piece has the ability to put other pieces in a state of confusion, causing them to randomly move within their own square instead of following the commands of the player. The Trickster is named as such because it can deceive and manipulate other pieces on the board, playing tricks and disrupting the opponent's strategy. The piece embodies the characteristics of a cunning and mischievous character, making it a formidable addition to any player's army

//----- ABILITY PIECES -----\\
// - "Disruptor":      A piece that can switch the places of any two pieces on the board, friend or foe.
// - "Laser":          A piece that can move in a straight line, capturing all pieces in its path, similar to a bishop.
// - "Dragon":         A piece that can move to any square on the board, but must "burn" the square it lands on, rendering it unusable for the rest of the game.
// - "Necromancer":    A piece that can control the dead, bringing captured pieces back to life and using them to fight for their side.
// - "Summoner":       A piece that can summon other pieces onto the board, allowing players to bring in reinforcements at any time.
// - "Plague-Bringer": A piece that spreads disease and decay, weakening other pieces and making them easier to capture.
// - "Alchemist":      A piece that can transmute other pieces, turning them into different, more powerful pieces.
// - "Hive-Mind":      A piece that can control multiple pieces at once, allowing players to move several pieces at once.
// - "Prophet":        A piece that can see into the future, allowing players to anticipate and plan for their opponent's moves.