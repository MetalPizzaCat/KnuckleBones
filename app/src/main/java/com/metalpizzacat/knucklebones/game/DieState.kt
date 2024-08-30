package com.metalpizzacat.knucklebones.game

import com.metalpizzacat.knucklebones.R
import kotlin.random.Random

enum class DieState(val value: Int, val res: Int) {
    NONE(0, R.drawable.dice_none),
    ONE(1, R.drawable.dice1),
    TWO(2, R.drawable.dice2),
    THREE(3, R.drawable.dice3),
    FOUR(4, R.drawable.dice4),
    FIVE(5, R.drawable.dice5),
    SIX(6, R.drawable.dice6);


    companion object {
        fun nextRandomDie(): DieState = DieState.entries[Random.nextInt(1, 7)]
    }
}

