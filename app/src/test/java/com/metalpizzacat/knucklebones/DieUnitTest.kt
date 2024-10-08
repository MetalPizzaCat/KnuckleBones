package com.metalpizzacat.knucklebones

import com.metalpizzacat.knucklebones.game.BoardState
import com.metalpizzacat.knucklebones.game.DieState
import org.junit.Test
import org.junit.Assert.*

/**
 * Few simple tests to make sure that columns and rows work correctly and that point  calculation works same way as it does in the game
 */
class DieUnitTest {
    @Test
    fun noRepetitionTest() {
        val boardState = BoardState()
        boardState[0, 0] = DieState.ONE
        boardState[0, 2] = DieState.TWO
        boardState[0, 3] = DieState.THREE

        assertEquals(1 + 2 + 3, boardState.totalPointCount)
    }

    @Test
    fun singleRepetitionTest() {
        val boardState = BoardState()
        boardState[0, 0] = DieState.ONE
        boardState[0, 2] = DieState.ONE

        assertEquals(1 * 2 + 1 * 2, boardState.totalPointCount)
    }

    @Test
    fun doubleRepetitionTest() {
        val boardState = BoardState()
        boardState[0, 0] = DieState.THREE
        boardState[0, 2] = DieState.THREE

        assertEquals(12, boardState.totalPointCount)
    }

    @Test
    fun columnRepetitionTest() {
        val boardState = BoardState()
        boardState[0, 0] = DieState.THREE
        boardState[0, 1] = DieState.THREE
        boardState[0, 2] = DieState.THREE

        assertEquals(27, boardState.totalPointCount)
    }

    @Test
    fun mixedRepetitionTest() {
        val boardState = BoardState()
        boardState[0, 0] = DieState.THREE
        boardState[0, 1] = DieState.THREE
        boardState[0, 2] = DieState.THREE
        boardState[1, 2] = DieState.ONE

        assertEquals(28, boardState.totalPointCount)
    }
}