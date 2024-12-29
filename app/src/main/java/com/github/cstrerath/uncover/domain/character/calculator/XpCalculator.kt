package com.github.cstrerath.uncover.domain.character.calculator

import kotlin.math.log10
import kotlin.math.pow

class XpCalculator {
    companion object {
        private const val INITIAL_XP = 0
        private const val SECOND_LEVEL_XP = 100
        private const val BASE_XP = 1000.0
        private const val LEVEL_DIVISOR = 23.0
    }

    fun calculateRequiredXp(level: Int): Int = when (level) {
        1 -> INITIAL_XP
        2 -> SECOND_LEVEL_XP
        else -> (BASE_XP * ((level - 1) / LEVEL_DIVISOR)
            .pow(log10(10.0) / log10(LEVEL_DIVISOR))).toInt()
    }

    fun getRequiredXpForNextLevel(currentLevel: Int): Int =
        calculateRequiredXp(currentLevel + 1)

    fun getRemainingXp(currentLevel: Int, currentXp: Int): Int =
        getRequiredXpForNextLevel(currentLevel) - currentXp
}