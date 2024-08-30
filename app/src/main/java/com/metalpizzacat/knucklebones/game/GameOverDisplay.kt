package com.metalpizzacat.knucklebones.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.metalpizzacat.knucklebones.R

@Composable
fun GameOverDisplay(
    playerWon: Boolean,
    playerPoints: Int,
    computerPoints: Int,
    onNewGameClicked: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xcc000000)) {
        Box {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (playerWon) {
                        stringResource(R.string.player_1_won)
                    } else {
                        stringResource(R.string.player_2_won)
                    },
                    textAlign = TextAlign.Center,
                    fontSize = 48.sp,
                    modifier = Modifier.fillMaxWidth(), color = Color.White
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xff000000))
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Player 1: ", fontSize = 24.sp, color = Color.White)
                    Text(text = playerPoints.toString(), fontSize = 24.sp, color = Color.White)
                    Text(text = " VS ", fontSize = 28.sp, color = Color.White)
                    Text(text = "Player 2: ", fontSize = 24.sp, color = Color.White)
                    Text(text = computerPoints.toString(), fontSize = 24.sp, color = Color.White)
                }
                Button(onClick = { onNewGameClicked() }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(R.string.to_the_menu))
                }
            }
        }
    }
}

@Preview
@Composable
fun GameOverPreview() {
    GameOverDisplay(playerWon = true, playerPoints = 28, computerPoints = 6) {}
}