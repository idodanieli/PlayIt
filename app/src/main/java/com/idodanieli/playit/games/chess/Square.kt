package com.idodanieli.playit.games.chess

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

class Square(val col: Int, val row: Int) {

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

    fun opposite(): Square {
        return Square(-1 * col, -1 * row)
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
    fun squaresPassedInMove(move: Square, excludeDestination: Boolean = false): List<Square> {
        var current = this.copy()
        var squares = mutableListOf<Square>()
        for (mCol in 1..move.col) {
            current = Square(current.col + mCol, current.row)
            squares.add(current)
        }
        for (mRow in 1..move.row) {
            current = Square(current.col, current.row + mRow)
            squares.add(current)
        }

        if (excludeDestination) { squares.remove(current) } // excludes the last squares from the moves

        return squares
    }

    fun copy(): Square {
        return Square(col, row)
    }

    fun isDark(): Boolean {
        return (col + row) % 2 == 1
    }

    fun isNear(other: Square, maxDistance: Int = 1): Boolean {
        return (Math.abs(col - other.col) <= maxDistance) && (Math.abs(row - other.row) <= maxDistance)
    }

    fun isValid(size: Int): Boolean {
        return col in 0..size && row in 0..size
    }

    fun isDiagonalDirection(): Boolean {
        return Math.abs(col) == Math.abs(row)
    }
}

fun greatestCommonDivider(a: Int, b: Int): Int {
    return if (b == 0) a else greatestCommonDivider(b, a % b)
}