package com.metalpizzacat.knucklebones

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.metalpizzacat.knucklebones.ui.theme.KnuckleBonesTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KnuckleBonesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Playfield(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun AnimatedDie(
    finalDie: DieState,
    cycleCount: Int,
    isActive: Boolean,
    delayBetweenSwitches: Long,
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
        tint = Color.Unspecified
    )
}

@Composable
fun DieIcon(die: DieState, onClick: () -> Unit) {
    IconButton(onClick = {
        if (die == DieState.NONE) {
            onClick()
        }
    }) {
        Icon(
            painterResource(die.res),
            "Die of value ${die.value}",
            tint = Color.Unspecified,
        )
    }
}

@Composable
fun DieGrid(board: BoardState, onClick: (column: Int) -> Unit) {
    Column {
        Text(board.totalPointCount.toString(), modifier = Modifier.fillMaxWidth())
        Row {
            for (j in 0..<3) {
                Column {
                    for (i in 0..<3) {
                        DieIcon(die = board[j, i]) {
                            Log.d("hi", "Clicked $j x $i")
                            if (board.canPlaceInColumn(j)) {
                                onClick(j)
                            }
                        }
                    }
                }
            }
        }
    }
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

    Column(modifier) {
        Text("Expected value: ${viewModel.expectedRoll.value}")
        if (viewModel.isPlayerTurn) {
            Text(text = "Player is doing a thing")
        } else {
            Text(text = "Wait for your turn")
        }
        DieGrid(board = computerBoardState) { }
        if (!viewModel.gameFinished) {
            AnimatedDie(
                viewModel.expectedRoll,
                cycleCount = currentAnimationCycleCount,
                isActive = isRolling,
                delayBetweenSwitches = 100
            ) {
                isRolling = false
                if (!viewModel.isPlayerTurn) {
                    scope.launch {
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
        DieGrid(board = playerBoardState) { y ->
            if (!isRolling && viewModel.isPlayerTurn) {
                viewModel.placeDieOnPlayerBoard(y, viewModel.expectedRoll)
                viewModel.doNextRoll()
                isRolling = true
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KnuckleBonesTheme {

    }
}