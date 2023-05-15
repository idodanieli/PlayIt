package com.idodanieli.playit.games.chess

class BitBoard {
    companion object {
        // Define constants for board size and square indices
        private const val BOARD_SIZE = 8
        private const val SQUARES_COUNT = BOARD_SIZE * BOARD_SIZE

        // 0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0
        // 1 0 0 0 0 0 0 0   0 1 0 0 0 0 0 0   0 0 1 0 0 0 0 0   0 0 0 1 0 0 0 0


        // Define masks for each square on the board
        val SQUARE_MASKS = ULongArray(SQUARES_COUNT) { 1UL shl it }

        // 0 0 0 0 0 0 0 0    0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0    0 0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0 0    0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0    0 0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0 0    0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0    0 0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0 0    0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0    0 0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0 0    0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0    1 1 1 1 1 1 1 1
        // 0 0 0 0 0 0 0 0    0 0 0 0 0 0 0 0   1 1 1 1 1 1 1 1    0 0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0 0    1 1 1 1 1 1 1 1   0 0 0 0 0 0 0 0    0 0 0 0 0 0 0 0
        // 1 1 1 1 1 1 1 1    0 0 0 0 0 0 0 0   0 0 0 0 0 0 0 0    0 0 0 0 0 0 0 0

        // This mask can be used in bitwise operations to isolate or manipulate specific ranks on a bitboard representation of a chessboard.
        // By applying this mask to a bitboard, you can extract the bits corresponding to the ranks and perform operations on them.
        private val RANK_MASKS = ULongArray(BOARD_SIZE) { 0xFFUL shl (it * BOARD_SIZE) }

        // 1 0 0 0 0 0 0 0   0 1 0 0 0 0 0 0   0 0 1 0 0 0 0 0   0 0 0 1 0 0 0 0
        // 1 0 0 0 0 0 0 0   0 1 0 0 0 0 0 0   0 0 1 0 0 0 0 0   0 0 0 1 0 0 0 0
        // 1 0 0 0 0 0 0 0   0 1 0 0 0 0 0 0   0 0 1 0 0 0 0 0   0 0 0 1 0 0 0 0
        // 1 0 0 0 0 0 0 0   0 1 0 0 0 0 0 0   0 0 1 0 0 0 0 0   0 0 0 1 0 0 0 0
        // 1 0 0 0 0 0 0 0   0 1 0 0 0 0 0 0   0 0 1 0 0 0 0 0   0 0 0 1 0 0 0 0
        // 1 0 0 0 0 0 0 0   0 1 0 0 0 0 0 0   0 0 1 0 0 0 0 0   0 0 0 1 0 0 0 0
        // 1 0 0 0 0 0 0 0   0 1 0 0 0 0 0 0   0 0 1 0 0 0 0 0   0 0 0 1 0 0 0 0
        // 1 0 0 0 0 0 0 0   0 1 0 0 0 0 0 0   0 0 1 0 0 0 0 0   0 0 0 1 0 0 0 0

        private val FILE_MASKS = ULongArray(BOARD_SIZE) { 0x0101010101010101UL shl it }

        // Function to get the bitboard for a specific square
        fun squareBitboard(square: Int): ULong {
            require(square in 0 until SQUARES_COUNT) { "Invalid square index" }
            return SQUARE_MASKS[square]
        }

        // Function to get the bitboard for a specific rank
        fun rankBitboard(rank: Int): ULong {
            require(rank in 0 until BOARD_SIZE) { "Invalid rank index" }
            return RANK_MASKS[rank]
        }

        // Function to get the bitboard for a specific file
        fun fileBitboard(file: Int): ULong {
            require(file in 0 until BOARD_SIZE) { "Invalid file index" }
            return FILE_MASKS[file]
        }

        // Function to print the bitboard as a visual representation
        fun printBitboard(bitboard: ULong) {
            for (rank in BOARD_SIZE - 1 downTo 0) {
                for (file in 0 until BOARD_SIZE) {
                    val square = rank * BOARD_SIZE + file
                    val mask = squareBitboard(square)
                    val pieceChar = if ((bitboard and mask) != 0UL) "1" else "0"
                    print("$pieceChar ")
                }
                println()
            }
        }
    }
}
