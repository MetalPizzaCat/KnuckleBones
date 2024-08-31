package com.metalpizzacat.knucklebones

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import com.metalpizzacat.knucklebones.game.GameViewModel
import com.metalpizzacat.knucklebones.game.Playfield


@Composable
fun TutorialPanel(modifier: Modifier = Modifier, gameViewModel: GameViewModel = viewModel()) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.knucklebones),
            fontSize = 10.em,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth()
        )
        Text(
            text = "(based on Cult of the Lamb version)",
            fontSize = 6.em,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.how_to_play),
            fontSize = 8.em,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth()
        )
        Row {
            Icon(Icons.Filled.Info, null)
            Text(text = "The game consists of two 3x3 boards, each belonging to their respective player")
        }
        Row {
            Icon(Icons.Filled.Info, null)
            Text(text = "The players take turns. On a player's turn, they roll a single 6-sided die, and must place it in a column on their board. A filled column does not accept any more dice")
        }
        Row {
            Icon(Icons.Filled.Info, null)
            Text(text = "Placing a die in a column will destroy all die of same value in the same column on other player's board")
        }
        Row {
            Icon(Icons.Filled.Info, null)
            Text(text = "Each player has a score, which is the sum of all the dice values on their board. The score awarded by each column is also displayed")
        }
        Row {
            Icon(Icons.Filled.Info, null)
            Text(text = "If there are several die of the same value in a column, their value gets multiplied by the amount of times they appear in that column")
        }
        Button(
            onClick = { gameViewModel.currentGameState = GameState.MENU },
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = "Okay")
        }
    }
}

@Composable
fun GameMenu(modifier: Modifier = Modifier, gameViewModel: GameViewModel = viewModel()) {


    AnimatedVisibility(visible = gameViewModel.currentGameState == GameState.MENU) {
        Box(modifier = modifier.fillMaxSize()) {
            Icon(
                painterResource(id = R.drawable.dice6),
                stringResource(R.string.image_of_a_die_with_6_dots),
                modifier = Modifier.align(Alignment.TopCenter),
                tint = Color.Unspecified
            )
            Row(modifier = Modifier.align(Alignment.Center)) {
                Button(
                    onClick = {
                        gameViewModel.startGameAgainstBot()
                    },
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(stringResource(R.string.play_against_bot))
                }
                Button(
                    onClick = {
                        gameViewModel.startGameAgainstPlayer()
                    },
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(stringResource(R.string.play_against_player))
                }
            }
            Button(
                onClick = {
                    gameViewModel.currentGameState = GameState.INFO
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(stringResource(R.string.how_to_play))
            }
        }
    }
    AnimatedVisibility(visible = gameViewModel.currentGameState == GameState.PLAYING) {
        Playfield(modifier = modifier.fillMaxWidth())
    }
    AnimatedVisibility(visible = gameViewModel.currentGameState == GameState.INFO) {
        TutorialPanel(modifier = modifier.fillMaxHeight())
    }
}