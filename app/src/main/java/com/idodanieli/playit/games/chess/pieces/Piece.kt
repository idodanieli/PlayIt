package com.idodanieli.playit.games.chess.pieces

import com.idodanieli.playit.games.chess.Board
import com.idodanieli.playit.games.chess.Player
import com.idodanieli.playit.games.chess.Square

const val NO_MAX_STEPS = 0

interface Piece {
    var square: Square
    val player: Player
    val type: Type
    val movementType: MovementType

    fun xrayPossibleMove(board: Board): List<Square>

    // possibleMoves returns all the squares a piece can move to, without taking general logic
    // into consideration like pinning, etc.
    fun possibleMoves(board: Board): List<Square>

    // validMoves returns a list of the squares the piece can move to
    fun validMoves(board: Board): List<Square>

    // onMove adds logic to piece after they have been moved
    fun onMove()

    // onEat adds logic to piece after they have eaten another piece
    fun onEat(eatenPiece: Piece)

    // canBeCaptured returns true if this piece could be captured by another piece on the board
    fun canBeCaptured(board: Board): Boolean
}

open class BasePiece(override var square: Square, override val player: Player): Piece {
    override val type = Type.NONE
    override val movementType = MovementType.NONE

    // possibleMoves returns all the squares a piece can move to, without taking general logic
    // into consideration like pinning, etc.
    override fun possibleMoves(board: Board): List<Square> {
        // To be overridden by child classes
        return emptyList()
    }

    override fun xrayPossibleMove(board: Board): List<Square> {
        return emptyList()
    }

    // validMoves returns a list of the squares the piece can move to
    override fun validMoves(board: Board): List<Square> {
        return if(isPinned(board)) emptyList() else possibleMoves(board)
    }

    override fun onMove() {
        return
    }

    override fun onEat(eatenPiece: Piece) {
        return
    }

    override fun toString(): String {
        return "$player $type at (${square.col}, ${square.row})"
    }

    // getAllAvailableMovesInDirection returns all the available moves in the given direction
    // must be used ONLY for continuous piece like: Rook, Bishop, Queen, etc.
    fun getAllAvailableMovesInDirection(board: Board, direction: Square, max_steps: Int = NO_MAX_STEPS): List<Square> {
        val moves = arrayListOf<Square>()
        var move = square + direction
        var steps = 0

        while (board.isIn(move) && (max_steps == NO_MAX_STEPS || steps < max_steps)) {
            when(board.playerAt(move)) {
                // a piece as same as the bishop
                player -> {
                    break
                }
                // an enemy piece
                player.opposite() -> {
                    moves.add(move)
                    break
                }
                else -> {
                    moves.add(move)
                    move += direction
                    steps += 1
                }
            }
        }

        return moves
    }

    fun getXrayMovesInDirection(board: Board, direction: Square, max_steps: Int = NO_MAX_STEPS): List<Square> {
        val moves = arrayListOf<Square>()
        var move = square + direction
        var steps = 0

        while (board.isIn(move) && (max_steps == 0 || steps < max_steps)) {
            moves.add(move)
            move += direction
            steps += 1
        }

        return moves
    }

    // isPinned returns true if the piece is pinned to the king
    private fun isPinned(board: Board): Boolean {
        if (type == Type.KING) { return false } // TODO: Make this more general

        val king = board.piece(Type.KING, this.player) ?: return false
        if (!king.square.isNear(square)) { return false }

        val direction = king.square.directionTo(square)

        var square = square.copy()
        while (square.isValid(board.size)) {
            square += direction

            board.pieceAt(square)?.let {
                if(it.player == player) { return false }
                if(king.square in it.xrayPossibleMove(board)) {
                    return true
                }
            }
        }

        return false
    }

    // canBeCaptured returns true if this piece could be captured by another piece on the board
    override fun canBeCaptured(board: Board): Boolean {
        val enemyPieces = board.pieces.filter { it.player == player.opposite() }
        for (enemyPiece in enemyPieces) {
            if (this.square in enemyPiece.validMoves(board)) { return true }
        }

        return false
    }
}

enum class Type {
    NONE,
    KING,
    QUEEN,
    ROOK,
    BISHOP,
    KNIGHT,
    PAWN,
    VENOM, // TODO: Change to parasite?
}

enum class MovementType {
    NONE,
    LEAPER,
    RIDER,
    HOPPER
}

// https://en.wikipedia.org/wiki/Fairy_chess_piece

/////// PIECES IDEAS \\\\\\\\
//----- SIMPLE PIECES -----\\
// - "The Duke":   A piece that resembles a pawn, but has a unique ability to only move one square forward diagonally
// - "Jumper":     A piece that can jump over an enemy piece to capture it, similar to a knight.
// - "Centaur":    A piece that can move as a knight or as a pawn.
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