package com.metalpizzacat.knucklebones.game

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

    var currentGameState by mutableStateOf(GameState.MENU)

    var expectedRoll by mutableStateOf(DieState.NONE)
        private set

    var gameFinished by mutableStateOf(false)
        private set

    var isPlayingAgainstPlayer by mutableStateOf(false)
        private set

    val playerWon: Boolean
        get() = _playerState.value.totalPointCount > _computerState.value.totalPointCount

    init {
        resetGame()
    }

    fun startGameAgainstPlayer() {
        resetGame()
        isPlayingAgainstPlayer = true
    }

    fun startGameAgainstBot() {
        resetGame()
        isPlayingAgainstPlayer = false
    }

    fun resetGame() {
        isPlayerTurn = true//Random.nextBoolean()
        expectedRoll = DieState.nextRandomDie()
        gameFinished = false
        _playerState.update {
            BoardState()
        }
        _computerState.update {
            BoardState()
        }
    }

    private fun finishGame() {
        gameFinished = true
    }

    private fun verifyGameState() {
        if (_playerState.value.boardFull || _computerState.value.boardFull) {
            finishGame()
        }
    }

    fun doNextRoll() {
        isPlayerTurn = !isPlayerTurn
        expectedRoll = DieState.nextRandomDie()
    }


    /**
     * Place a given die to the specified column
     */
    fun placeDieOnPlayerBoard(columnId: Int, die: DieState) {
        if (_playerState.value.boardFull) {
            return
        }
        _playerState.update {
            it.addAndCopy(columnId, die)
        }
        _computerState.update {
            it.removeAndCopy(columnId, die)
        }
        verifyGameState()
    }

    /**
     * Place a die on the computer board the same way it can be placed on the player bird
     */
    fun placeDieOnComputerBoard(columnId: Int, die: DieState) {
        if (_computerState.value.boardFull) {
            return
        }
        _computerState.update {
            it.addAndCopy(columnId, die)
        }
        _playerState.update {
            it.removeAndCopy(columnId, die)
        }
        verifyGameState()
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
        }
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
