package com.github.cstrerath.uncover.domain.character.calculator

import android.util.Log
import kotlin.math.log10
import kotlin.math.pow

class XpCalculator {
    private val tag = "XpCalculator"

    fun calculateRequiredXp(level: Int): Int {
        Log.d(tag, "Calculating required XP for level: $level")
        return when (level) {
            1 -> INITIAL_XP
            2 -> SECOND_LEVEL_XP
            else -> calculateComplexXp(level)
        }.also {
            Log.d(tag, "Required XP for level $level: $it")
        }
    }

    private fun calculateComplexXp(level: Int): Int {
        return (BASE_XP * ((level - 1) / LEVEL_DIVISOR)
            .pow(log10(10.0) / log10(LEVEL_DIVISOR))).toInt()
    }

    fun getRequiredXpForNextLevel(currentLevel: Int): Int {
        Log.d(tag, "Calculating required XP for next level from current level: $currentLevel")
        return calculateRequiredXp(currentLevel + 1)
    }

    fun getRemainingXp(currentLevel: Int, currentXp: Int): Int {
        Log.d(tag, "Calculating remaining XP. Level: $currentLevel, Current XP: $currentXp")
        return getRequiredXpForNextLevel(currentLevel).minus(currentXp).also {
            Log.d(tag, "Remaining XP: $it")
        }
    }

    companion object {
        private const val INITIAL_XP = 0
        private const val SECOND_LEVEL_XP = 100
        private const val BASE_XP = 1000.0
        private const val LEVEL_DIVISOR = 23.0
    }
}
