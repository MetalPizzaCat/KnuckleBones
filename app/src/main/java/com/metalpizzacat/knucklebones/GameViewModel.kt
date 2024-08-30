package com.metalpizzacat.knucklebones

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class GameViewModel : ViewModel() {
    private val _playerState = MutableStateFlow(BoardState())
    val playerState: StateFlow<BoardState> = _playerState.asStateFlow()

    private val _computerState = MutableStateFlow(BoardState())
    val computerState: StateFlow<BoardState> = _computerState.asStateFlow()

    var isPlayerTurn by mutableStateOf(true)
        private set

    var expectedRoll by mutableStateOf(DieState.NONE)
        private set

    var gameFinished by mutableStateOf(false)
        private set

    init {
        resetGame()
    }

    fun resetGame() {
        isPlayerTurn = true//Random.nextBoolean()
        expectedRoll = DieState.nextRandomDie()
        gameFinished = false
    }

    private fun finishGame() {
        gameFinished = true
    }

    fun doNextRoll() {
        isPlayerTurn = !isPlayerTurn
        expectedRoll = DieState.nextRandomDie()
    }


    /**
     * Place a given die to the specified column
     */
    fun placeDieOnPlayerBoard(columnId: Int, die: DieState) {
        _playerState.update {
            it.addAndCopy(columnId, die)
        }
        _computerState.update {
            it.removeAndCopy(columnId, die)
        }
        if (_playerState.value.boardFull) {
            finishGame()
        }
    }

    /**
     * Place a die on the computer board
     */
    fun doComputerTurn() {
        // benefit points will count as the resulting difference between player and computer points after a possible move
        val benefitPoints: MutableList<Int> = MutableList(3) { 0 }


        for (i in 0..<3) {
            if (!_computerState.value.canPlaceInColumn(i)) {
                continue
            }
            // make a copy so we could alter each column and just call the calculation function
            val copyBoard = computerState.value.deepCopy()
            val enemyCopyBoard = playerState.value.deepCopy()

            copyBoard.add(i, expectedRoll)
            enemyCopyBoard.remove(i, expectedRoll)

            benefitPoints[i] =
                (copyBoard.totalPointCount - enemyCopyBoard.totalPointCount)
            Log.d("CAC_STEP", "Column: $i predicted: ${benefitPoints[i]}")
        }
        Log.d("COMPUTER_AI_CALC", benefitPoints.joinToString(","))
        val resultColumn =
            benefitPoints.indices.filter { benefitPoints[it] != 0 }.maxBy { benefitPoints[it] }
        _computerState.update {
            it.addAndCopy(resultColumn, expectedRoll)
        }
        _playerState.update {
            it.removeAndCopy(resultColumn, expectedRoll)
        }
        if (_computerState.value.boardFull) {
            finishGame()
        }
    }
}
