package com.metalpizzacat.knucklebones

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

    var removedPlayerDice : HashMap<Int, DieState> = HashMap()
        private set

    var isPlayerTurn by mutableStateOf(true)
        private set

    var expectedRoll by mutableStateOf(DieState.NONE)
        private set

    init {
        resetGame()
    }

    fun resetGame() {
        isPlayerTurn = true//Random.nextBoolean()
        expectedRoll = DieState.nextRandomDie()
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
    }

    fun doComputerTurn(){
        
    }

//    fun placeDieOnComputerBoard(columnId: Int, die: DieState) {
//        val board: Array<Array<DieState>> =
//            tryAddDieToBoard(die, columnId, _gameState.value.computerBoard) ?: return
//        _gameState.update { current ->
//            current.copy(computerBoard = board)
//        }
//    }
}