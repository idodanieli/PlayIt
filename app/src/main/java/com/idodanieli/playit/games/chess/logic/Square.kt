package com.idodanieli.playit.games.chess.logic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val DIRECTIONS = mutableMapOf<String, Square>(
    "W" to Square(1 ,0),
    "E" to Square(-1 ,0),
    "N" to Square(0 ,1),
    "S" to Square(0 ,-1),
    "SW" to Square(1 ,-1),
    "NW" to Square(1 ,1),
    "SE" to Square(-1 ,-1),
    "NE" to Square(-1 ,1),
)


@Serializable
data class Square(
    @SerialName("file") val col: Int,
    @SerialName("rank") val row: Int
) {
    companion object {
        fun from_bitboard(bitboard: ULong): Square {
            return Square(BitBoard.getFile(bitboard), BitBoard.getRank(bitboard))
        }
    }

    override fun equals(other: Any?): Boolean =
        (other is Square) && this.col == other.col && this.row == other.row

    override fun hashCode(): Int {
        var result = col
        result = 31 * result + row
        return result
    }

    override fun toString(): String {
        return "($col, $row)"
    }

    operator fun plus(other: Square) = Square(col + other.col, row + other.row)

    operator fun minus(other: Square) = Square(col - other.col, row - other.row)

    fun directionTo(other: Square): Square {
        val colDiff = other.col - col
        val rowDiff = other.row - row
        val gcd = greatestCommonDivider(Math.abs(colDiff), Math.abs(rowDiff))
        return Square(colDiff / gcd, rowDiff / gcd)
    }

    // squaresBetween returns all the squares between this square and the other square
    // if the direction os (2, 1) it will jump (2, 1) between iterations
    fun squaresBetween(destination: Square, excludeDestination: Boolean = false): List<Square> {
        val squares = mutableListOf<Square>()
        val direction = directionTo(destination)
        var current = this.copy()
        while (current != destination) {
            current += direction
            squares.add(current)
        }

        if (excludeDestination) { squares.remove(current) } // excludes the last squares from the moves

        return squares
    }

    // squaresPassedInMove returns all the squares between this square and the other square
    // if the direction is (2, 1) it will jump return (1, 0) (2, 0) (2, 1)
    fun squaresPassedInMove(move: Square, moveByRowFirst: Boolean, excludeDestination: Boolean = false): List<Square> {
        var current = this.copy()
        val squares = mutableListOf<Square>()

        if (moveByRowFirst) {
            for (mRow in 1..move.row) {
                current = Square(current.col, current.row + 1)
                squares.add(current)
            }
        }

        for (mCol in 1..move.col) {
            current = Square(current.col + 1, current.row)
            squares.add(current)
        }

        if (!moveByRowFirst) {
            for (mRow in 1..move.row) {
                current = Square(current.col, current.row + 1)
                squares.add(current)
            }
        }

        if (excludeDestination) { squares.remove(current) } // excludes the last squares from the moves

        return squares
    }

    // neighbors returns all the squares near the given piece
    fun neighbors(): List<Square> {
        val squares = arrayListOf<Square>()

        for (i in arrayOf(-1, 0, 1)) {
            for (j in arrayOf(-1, 0, 1)) {
                if (i == 0 && j == 0) { continue }

                val square = Square(col + i, row + j)

                squares.add(square)
            }
        }

        return squares
    }

    fun copy(): Square {
        return Square(col, row)
    }

    fun isNear(other: Square, maxDistance: Int = 1): Boolean {
        return (Math.abs(col - other.col) <= maxDistance) && (Math.abs(row - other.row) <= maxDistance)
    }

    fun inBorder(size: Int): Boolean {
        return col in 0 until size && row in 0 until size
    }

    fun bitboard(): ULong {
        return BitBoard.squareBitboard(row * 8 + col) // TODO: 8 should be BOARD_SIZE
    }

    // flipsVertically flips the square vertically
    fun flipVertically(boardSize: Int): Square {
        val flippedRow = boardSize - (row + 1)
        return Square(col, flippedRow)
    }

    // --- Directions ------------------------------------------------------------
    fun isDiagonalDirection(): Boolean {
        return Math.abs(col) == Math.abs(row)
    }

    fun isVerticalDirection(): Boolean {
        return row != 0 && col == 0
    }

    fun isHorizontalDirection(): Boolean {
        return row == 0 && col != 0
    }

}

fun greatestCommonDivider(a: Int, b: Int): Int {
    return if (b == 0) a else greatestCommonDivider(b, a % b)
}