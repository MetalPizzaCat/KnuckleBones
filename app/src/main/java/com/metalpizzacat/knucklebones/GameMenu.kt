package com.metalpizzacat.knucklebones

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.metalpizzacat.knucklebones.game.GameState
import com.metalpizzacat.knucklebones.game.GameViewModel
import com.metalpizzacat.knucklebones.game.Playfield


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
                        gameViewModel.currentGameState = GameState.SINGLE
                        gameViewModel.startGameAgainstBot()
                    },
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(stringResource(R.string.play_against_bot))
                }
                Button(
                    onClick = {
                        gameViewModel.currentGameState = GameState.MULTI
                        gameViewModel.startGameAgainstPlayer()
                    },
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(stringResource(R.string.play_against_player))
                }
            }
        }
    }
    AnimatedVisibility(visible = gameViewModel.currentGameState != GameState.MENU) {
        Playfield(modifier = modifier.fillMaxWidth())
    }
}