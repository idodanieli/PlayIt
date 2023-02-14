package com.idodanieli.playit.games.chess

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

    // squaresBetween returns all the squares between this square and the other square
    fun squaresBetween(other: Square, excludeOther: Boolean = false): List<Square> {
        val squares = mutableListOf<Square>()
        val direction = directionTo(other)
        var current = this.copy()
        while (current != other) {
            current += direction
            squares.add(current)
        }

        if (excludeOther) { squares.remove(current) } // excludes the last squares from the moves

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
}

fun greatestCommonDivider(a: Int, b: Int): Int {
    return if (b == 0) a else greatestCommonDivider(b, a % b)
}