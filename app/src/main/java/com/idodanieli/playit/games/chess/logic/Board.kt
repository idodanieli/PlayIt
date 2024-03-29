package com.idodanieli.playit.games.chess.logic

import com.idodanieli.playit.games.chess.CHESSBOARD_SIZE
import com.idodanieli.playit.games.chess.pieces.*
import com.idodanieli.playit.games.chess.pieces.classic.TYPE_KING

val DEFAULT_DIMENSIONS = BoardDimensions(CHESSBOARD_SIZE, CHESSBOARD_SIZE)

data class BoardDimensions(val cols: Int, val rows: Int)

class Board(startingPieces: Set<Piece>, val dimensions: BoardDimensions = DEFAULT_DIMENSIONS) {
    lateinit var map: MutableMap<Square, Piece>
    lateinit var whitePieces: MutableMap<Piece, Boolean>
    lateinit var blackPieces: MutableMap<Piece, Boolean>

    init {
        setPieces(startingPieces)
    }

    // pieceAt returns the piece at the given square. if there is none - returns null
    fun pieceAt(square: Square): Piece? {
        return map[square]
    }

    // playerAt returns the player at the given square
    fun playerAt(square: Square): Player? {
        return pieceAt(square)?.player
    }

    // piece returns the first it finds that if of the given players and of the given type
    fun getPiece(type: String, player: Player): Piece? {
        return when (player) {
            Player.BLACK -> getPieceByType(blackPieces, type)
            Player.WHITE -> getPieceByType(whitePieces, type)
        }
    }

    fun pieces(player: Player): MutableSet<Piece> {
        return if (player.isWhite()) whitePieces.keys else blackPieces.keys
    }
    fun pieces(): Set<Piece> {
        return whitePieces.keys + blackPieces.keys
    }

    fun setPieces(pieces: Set<Piece>) {
        map = pieces.associateBy { it.square }.toMutableMap()
        whitePieces = pieces.filter { it.player.isWhite() }.associateWith { true }.toMutableMap()
        blackPieces = pieces.filter { it.player.isBlack() }.associateWith { true }.toMutableMap()
    }

    // moves the piece to the destination
    fun move(piece: Piece, dst: Square) {
        map.remove(piece.square)
        map[dst] = piece

        piece.square = dst
        piece.onMove()
    }
    fun move(move: Move) {
        pieceAt(move.origin)?.let {
            move(it, move.dest)
        }
    }
    fun move(moves: List<Move>) {
        for (move in moves) {
            this.move(move)
        }
    }

    // removes a piece from the board
    fun remove(piece: Piece) {
        map.remove(piece.square)

        if (piece.player.isWhite()) {
            whitePieces.remove(piece)
            return
        }

        blackPieces.remove(piece)
    }

    // add a piece to the board
    fun add(piece: Piece) {
        map[piece.square] = piece

        if (piece.player.isWhite()) {
            whitePieces[piece] = true
            return
        }

        blackPieces[piece] = true
    }

    // isIn returns true if the given square is in the boards borders
    fun isIn(square: Square): Boolean {
        return square.col in 0 until this.dimensions.cols && square.row in 0 until this.dimensions.rows
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

            if (square in piece.capturableSquares(this)) {
                return true
            }
        }

        return false
    }

    // isFreeAndSafe returns true if the square isFree and not isThreatened
    fun isFreeAndSafe(square: Square, enemy: Player): Boolean {
        return isFree(square) && !isThreatened(square, enemy)
    }

    // canBeCaptured returns true if this piece could be captured by another piece on the board
    fun canBeCaptured(piece: Piece): Boolean {
        return getPotentialEaters(piece).isNotEmpty()
    }

    // getPotentialEaters returns all the pieces that can capture the given piece on the next move
    fun getPotentialEaters(piece: Piece): List<Piece> {
        return pieces(piece.player.opposite()).filter {
            piece.square in it.capturableSquares(this)
        }
    }

    // neighborSquares returns all the available squares near the given piece
    fun neighborSquares(piece: Piece): List<Square> {
        return piece.square.neighbors().filter {
            isIn(it) // && playerAt(it) != piece.player
        }
    }

    fun neighborPieces(piece: Piece): Collection<Piece> {
        return neighborSquares(piece)
            .filter { pieceAt(it) != null }
            .map { pieceAt(it)!! }
    }

    // --- FOR PRINTING THE BOARD ------------------------------------------------------------------
    private fun flatString(pieces: List<Piece>) : String {
        val flatBoardCharacters = ".".repeat(dimensions.rows * dimensions.cols).toCharArray()
        for (piece in pieces) {
            val type = if (piece.player.isWhite()) piece.type else piece.type.lowercase()
            val index = (dimensions.rows - (piece.square.row + 1)) * dimensions.cols + piece.square.col
            flatBoardCharacters[index] = type[0]
        }

        return String(flatBoardCharacters)
    }

    private fun flatToPrettyPrint(flat: String) : String {
        var prettyCharacters = charArrayOf()

        for (idx in flat.indices) {
            if (idx % dimensions.cols == 0) {
                prettyCharacters += '\n'
            }

            prettyCharacters += flat[idx]
            prettyCharacters += ' '
        }

        return String(prettyCharacters)
    }

    override fun toString() : String {
        val flatGameRepresentation = flatString(this.pieces().toList())
        return flatToPrettyPrint(flatGameRepresentation)
    }

    // --- Utils -----------------------------------------------------------------------------------
    fun getRandomPieceOfPlayer(player: Player): Piece {
        if (player.isWhite()) {
            return random( whitePieces.keys )
        }

        return random( blackPieces.keys )
    }

    // ---------------------------------------------------------------------------------------------
    fun copy(): Board {
        val copiedPieces = deepCopyPieces(this.pieces())
        return Board(copiedPieces, dimensions)
    }
}