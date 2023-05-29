package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.pieces.*
import com.idodanieli.playit.games.chess.pieces.classic.TYPE_KING
import com.idodanieli.playit.games.chess.pieces.core.MovementType

class Board(var pieces: MutableSet<Piece>, var size: Int) {
    var map = pieces.associateBy { it.square }.toMutableMap()
    var whitePieces = pieces.filter { it.player == Player.WHITE }.associateWith { true }.toMutableMap()
    var blackPieces = pieces.filter { it.player == Player.BLACK }.associateWith { true }.toMutableMap()

    // pieceAt returns the piece at the given square. if there is none - returns null
    fun pieceAt(square: Square): Piece? {
        return map[square]
    }

    // pieceAt returns the piece at square if its of the same player
    // if there is none - returns null
    fun pieceAt(square: Square, player: Player) : Piece? {
        val piece = pieceAt(square) ?: return null
        if (piece.player != player) { return null }

        return piece
    }

    // playerAt returns the player at the given square
    fun playerAt(square: Square): Player? {
        return pieceAt(square)?.player
    }

    // piece returns the first it finds that if of the given players and of the given type
    fun piece(type: String, player: Player): Piece? {
        for (piece in pieces) {
            if (piece.player == player && piece.type == type) {
                return piece
            }
        }

        return null
    }

    // pieces returns all the pieces of the given player
    fun pieces(player: Player): MutableSet<Piece> {
        return if (player == Player.WHITE) whitePieces.keys else blackPieces.keys
    }

    // moves the piece to the destination
    fun move(piece: Piece, dst: Square) {
        map.remove(piece.square)
        map[dst] = piece

        piece.square = dst
        piece.onMove()
    }

    // removes a piece from the board
    fun remove(piece: Piece) {
        pieces.remove(piece)
        map.remove(piece.square)

        if (piece.player == Player.WHITE) {
            whitePieces.remove(piece)
            return
        }

        blackPieces.remove(piece)
    }

    // isIn returns true if the given square is in the boards borders
    fun isIn(square: Square): Boolean {
        return square.col in 0 until this.size && square.row in 0 until this.size
    }

    // isFree returns true if the given square doesn't contain a piece
    fun isFree(square: Square): Boolean {
        return pieceAt(square) == null
    }

    fun isFree(squares: List<Square>): Boolean {
        for(square in squares) {
            if (!isFree(square)) { return false }
        }

        return true
    }

    // isThreatened returns true if the square is threatened by another piece
    fun isThreatened(square: Square, enemy: Player): Boolean {
        for (piece in pieces(enemy)) {
            if (piece.type == TYPE_KING) { // To avoid recursion
                if (piece.square.isNear(square)) return true
                continue
            }

            if (square in piece.captureMoves(this)) {
                return true
            }
        }

        return false
    }

    // canBeCaptured returns true if this piece could be captured by another piece on the board
    fun canBeCaptured(piece: Piece): Boolean {
        return getPotentialEaters(piece).isNotEmpty()
    }

    // getPotentialEaters returns all the pieces that can capture the given piece on the next move
    fun getPotentialEaters(piece: Piece): List<Piece> {
        return pieces(piece.player.opposite()).filter {
            piece.square in it.captureMoves(this)
        }
    }

    private val MOVE_OFFSETS = arrayOf(-1, 0, 1)

    // getNeighborSquares returns all the squares near the given piece
    fun getAvailableNeighborSquares(piece: Piece): List<Square> {
        val squares = arrayListOf<Square>()

        for (i in MOVE_OFFSETS) {
            for (j in MOVE_OFFSETS) {
                if (i == 0 && j == 0) { continue }

                val square = Square(piece.square.col + i, piece.square.row + j)

                if (isIn(square) && playerAt(square) != piece.player) {
                    squares.add(square)
                }
            }
        }

        return squares
    }

    // getPinner returns the piece that pins the current piece
    fun getPinner(pinned: Piece) : Piece? {
        if (pinned.type == TYPE_KING) { return null } // TODO: Make this more general

        val king = piece(TYPE_KING, pinned.player) ?: return null
        val direction = king.square.directionTo(pinned.square)

        var currentSquare = king.square.copy()
        var passedPinnedPieceSquare = false

        while (currentSquare.inBorder(size)) {
            currentSquare += direction

            pieceAt(currentSquare)?.let {currentPiece ->
                if (!passedPinnedPieceSquare) {
                    if (currentPiece == pinned) { passedPinnedPieceSquare = true }
                    else {
                        // EXAMPLE: k . b p . . . R . ( the bishop blocks the pin for the pawn )
                        // Another piece blocks the pin ( before )
                        return null
                    }
                } else {
                    // Here we are after the pinned piece ( no piece blocks before )
                    if (currentPiece.player == pinned.player) {
                        // EXAMPLE: K . . p b . . R . ( the bishop blocks the pin for the pawn )
                        // Another piece blocks the pin ( afterwards )
                        return null
                    }

                    if(currentPiece.movementType == MovementType.RIDER &&
                        king.square in currentPiece.xrayPossibleMove(this)) {
                        // The piece is pinned to the currentPiece
                        // Example: k . . p . . . R . ( the pawn is pinned by the rook )
                        return currentPiece
                    }

                    // EXAMPLE: K . . p b . . R . ( the bishop blocks the pin for the pawn )
                    // Another piece blocks the pin ( afterwards )
                    return  null
                }
            }
        }

        return null
    }

    private fun flatString(pieces: List<Piece>) : String {
        val flatBoardCharcters = ".".repeat(size * size).toCharArray()
        for (piece in pieces) {
            val type = if (piece.player == Player.WHITE) piece.type else piece.type.lowercase()
            flatBoardCharcters[piece.square.row * size + piece.square.col] = type[0]
        }

        return String(flatBoardCharcters)
    }

    private fun flatToPrettyPrint(flat: String) : String {
        var prettyCharacters = charArrayOf()

        for (idx in flat.indices) {
            if (idx % size == 0) {
                prettyCharacters += '\n'
            }

            prettyCharacters += flat[idx]
            prettyCharacters += ' '
        }

        return String(prettyCharacters)
    }

    override fun toString() : String {
        return flatToPrettyPrint(flatString(pieces.toList()))
    }

    fun flipMoveVertically(move: Move): Move {
        return Move(
            origin= move.origin.flipVertically(size),
            dest = move.dest.flipVertically(size),
            player= move.player
        )
    }
}