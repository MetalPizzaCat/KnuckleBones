package com.metalpizzacat.knucklebones.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metalpizzacat.knucklebones.GameState
import com.metalpizzacat.knucklebones.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


@Composable
fun AnimatedDie(
    finalDie: DieState,
    cycleCount: Int,
    isActive: Boolean,
    delayBetweenSwitches: Long,
    modifier: Modifier = Modifier,
    onCycleFinished: () -> Unit
) {

    var count by remember { mutableIntStateOf(0) }
    var currentDie by remember { mutableStateOf(DieState.ONE) }
    LaunchedEffect(cycleCount, isActive) {
        while (count < cycleCount && isActive) {
            delay(delayBetweenSwitches)
            count++
            if (count == cycleCount - 1) {
                currentDie = finalDie
                onCycleFinished()
                count = 0
            } else {
                val value = Random.nextInt(1, 7)
                currentDie = DieState.entries.first { p -> p.value == value }
            }
        }
    }
    Icon(
        painterResource(currentDie.res),
        "Animated die of value ${currentDie.value}",
        tint = Color.Unspecified,
        modifier = modifier
    )
}

@Composable
fun Playfield(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val currentAnimationCycleCount by remember { mutableIntStateOf(9) }
    var isRolling by remember { mutableStateOf(true) }
    val playerBoardState by viewModel.playerState.collectAsState()
    val computerBoardState by viewModel.computerState.collectAsState()
    val scope = rememberCoroutineScope()



    Column(
        modifier
            .fillMaxSize()
    ) {
        DieGrid(
            board = computerBoardState,
            mirrored = true,
            isCurrentTurn = !viewModel.isPlayerTurn,
            modifier = Modifier.weight(0.4f)
        ) { y ->
            if (viewModel.isPlayingAgainstPlayer && !isRolling && !viewModel.isPlayerTurn) {
                viewModel.placeDieOnComputerBoard(y, viewModel.expectedRoll)
                viewModel.doNextRoll()
                isRolling = true
            }
        }
        if (!viewModel.gameFinished) {
            AnimatedDie(
                viewModel.expectedRoll,
                cycleCount = currentAnimationCycleCount,
                isActive = isRolling,
                delayBetweenSwitches = 100,
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                isRolling = false
                if (!viewModel.isPlayingAgainstPlayer && !viewModel.isPlayerTurn) {
                    scope.launch {
                        // this is just so that the player would have *some* time to read dice value
                        delay(1000)
                        viewModel.doComputerTurn()
                        viewModel.doNextRoll()
                        isRolling = true
                    }

                }
            }
        } else {
            Button(onClick = { viewModel.resetGame() }) {
                Text(text = stringResource(R.string.start_over))
            }
        }
        DieGrid(
            board = playerBoardState,
            mirrored = false,
            isCurrentTurn = viewModel.isPlayerTurn,
            modifier = Modifier.weight(0.4f)
        ) { y ->
            if (!isRolling && viewModel.isPlayerTurn) {
                viewModel.placeDieOnPlayerBoard(y, viewModel.expectedRoll)
                viewModel.doNextRoll()
                isRolling = true
            }
        }
    }
    AnimatedVisibility(visible = viewModel.gameFinished, modifier = Modifier.fillMaxWidth()) {
        GameOverDisplay(
            playerWon = viewModel.playerWon,
            playerPoints = playerBoardState.totalPointCount,
            computerPoints = computerBoardState.totalPointCount
        ) {
            viewModel.currentGameState = GameState.MENU
        }
    }
}
