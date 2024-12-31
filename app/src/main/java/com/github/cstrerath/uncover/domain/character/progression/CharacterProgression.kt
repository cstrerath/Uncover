package com.github.cstrerath.uncover.domain.character.progression

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.calculator.StatCalculator
import com.github.cstrerath.uncover.domain.character.calculator.XpCalculator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CharacterProgression(context: Context) {
    private val tag = "CharacterProgression"
    private val repository = CharacterRepository(context)
    private val xpCalculator = XpCalculator()
    private val statCalculator = StatCalculator()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    private val xpManager = XpManager(repository)
    private val levelUpManager = LevelUpManager(repository, xpCalculator, statCalculator)

    fun getRequiredXpForNextLevel(currentLevel: Int): Int {
        Log.d(tag, "Calculating required XP for level ${currentLevel + 1}")
        return xpCalculator.getRequiredXpForNextLevel(currentLevel)
    }

    fun getRemainingXp(currentLevel: Int, currentXp: Int): Int {
        Log.d(tag, "Calculating remaining XP for level $currentLevel (Current: $currentXp)")
        return xpCalculator.getRemainingXp(currentLevel, currentXp)
    }

    fun addTestXp() {
        Log.d(tag, "Adding test XP")
        coroutineScope.launch {
            try {
                xpManager.addTestXp()
            } catch (e: Exception) {
                Log.e(tag, "Failed to add test XP: ${e.message}")
            }
        }
    }

    fun tryLevelUp() {
        Log.d(tag, "Attempting level up")
        coroutineScope.launch {
            try {
                levelUpManager.tryLevelUp()
            } catch (e: Exception) {
                Log.e(tag, "Failed to level up: ${e.message}")
            }
        }
    }
}