package com.metalpizzacat.knucklebones.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metalpizzacat.knucklebones.R


@Composable
fun DieIcon(die: DieState, modifier: Modifier = Modifier) {
    Icon(
        painterResource(die.res),
        "Die of value ${die.value}",
        tint = Color.Unspecified,
        modifier = modifier
            .padding(5.dp)
            .fillMaxSize()
    )

}

@Composable
fun DieColumn(
    column: List<DieState>,
    mirrored: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick() }
    ) {
        if (mirrored) {
            for (die in column.filter { it == DieState.NONE })
                DieIcon(
                    die = die, modifier = Modifier
                        .weight(0.3f)
                        .fillMaxSize()
                )
            for (die in column.filter { it != DieState.NONE })
                DieIcon(
                    die = die, modifier = Modifier
                        .weight(0.3f)
                        .fillMaxSize()
                )

        } else {
            for (die in column.filter { it != DieState.NONE })
                DieIcon(
                    die = die, modifier = Modifier
                        .weight(0.3f)
                        .fillMaxSize()
                )
            for (die in column.filter { it == DieState.NONE })
                DieIcon(
                    die = die, modifier = Modifier
                        .weight(0.3f)
                        .fillMaxSize()
                )
        }
    }
}


@Composable
fun DieGrid(
    modifier: Modifier = Modifier,
    isCurrentTurn: Boolean,
    board: BoardState,
    mirrored: Boolean,
    onClick: (column: Int) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (!mirrored) {
            Text(
                text = board.totalPointCount.toString(),
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        }
        Row(
            modifier = if (!isCurrentTurn) {
                Modifier
            } else {
                Modifier
                    .background(color = colorResource(id = R.color.teal_700))
            }
                .fillMaxWidth()
                .weight(0.9f)
        ) {
            for (j in 0..<3) {
                DieColumn(
                    column = if (mirrored) {
                        listOf(board[j, 2], board[j, 1], board[j, 0])
                    } else {
                        listOf(board[j, 0], board[j, 1], board[j, 2])
                    },
                    mirrored = mirrored,
                    modifier = Modifier.weight(0.3f)
                ) {
                    onClick(j)
                }
            }
        }
        if (mirrored) {
            Text(
                text = board.totalPointCount.toString(),
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        }
    }
}
