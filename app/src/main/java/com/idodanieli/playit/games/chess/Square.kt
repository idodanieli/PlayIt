package com.idodanieli.playit.games.chess

class Square(val col: Int, val row: Int) {

    override fun equals(other: Any?): Boolean =
        (other is Square) && this.col == other.col && this.row == other.row

    override fun hashCode(): Int {
        var result = col
        result = 31 * result + row
        return result
    }

    operator fun plus(other: Square) = Square(col + other.col, row + other.row)

    fun isDark(): Boolean {
        return (col + row) % 2 == 1
    }
}

