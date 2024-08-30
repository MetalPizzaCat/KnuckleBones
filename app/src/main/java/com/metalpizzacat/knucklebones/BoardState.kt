package com.metalpizzacat.knucklebones

data class BoardState(private val board: Array<DieState> = Array(9) { DieState.NONE }) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BoardState

        return board.contentDeepEquals(other.board)
    }

    override fun hashCode(): Int {
        return board.contentDeepHashCode()
    }

    operator fun get(column: Int, row: Int) = board[row + column * 3]

    operator fun set(column: Int, row: Int, die: DieState) {
        board[row + column * 3] = die
    }

    val boardFull: Boolean
        get() = board.all { it != DieState.NONE }

    val totalPointCount: Int
        get() {
            var total: Int = 0

            for (column in board.toList().chunked(3)) {
                for (i in 0..<3) {
                    total += column[i].value * column.count { it == column[i] }
                }
            }
            return total
        }

    /**
     * Create a new copy of the board state with new die added at the specified column
     * @param column which to append to
     * @param die what die value to write
     */
    fun addAndCopy(column: Int, die: DieState): BoardState {
        val copyArray = board.clone()
        for (i in 0..<3) {
            if (this[column, i] == DieState.NONE) {
                copyArray[i + column * 3] = die
                break
            }
        }
        return copy(board = copyArray)
    }

    fun add(column: Int, die: DieState) {
        for (i in 0..<3) {
            if (this[column, i] == DieState.NONE) {
                this[column, i] = die
                return
            }
        }
    }

    fun remove(column: Int, die: DieState) {
        for (i in 0..<3) {
            if (this[column, i] == die) {
                this[column, i] = DieState.NONE
            }
        }
    }

    /**
     * Create a new copy of the board state with all die matching the given die removed.
     * If there is nothing to remove this will be equivalent to calling `deepCopy`
     */
    fun removeAndCopy(column: Int, die: DieState): BoardState {
        val copyArray = board.clone()
        for (i in 0..<3) {
            if (copyArray[i + column * 3] == die) {
                copyArray[i + column * 3] = DieState.NONE
            }
        }
        return copy(board = copyArray)
    }

    /**
     * Check if a new die can be added to a column
     * @param column Column to check
     */
    fun canPlaceInColumn(column: Int): Boolean = (0..<3).any { this[column, it] == DieState.NONE }


    fun deepCopy(): BoardState = copy(board = board.clone())
}
