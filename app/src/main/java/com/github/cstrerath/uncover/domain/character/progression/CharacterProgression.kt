package com.github.cstrerath.uncover.domain.character.progression

import android.content.Context
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.calculator.StatCalculator
import com.github.cstrerath.uncover.domain.character.calculator.XpCalculator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// CharacterProgression.kt
class CharacterProgression(context: Context) {
    private val repository = CharacterRepository(context)
    private val xpCalculator = XpCalculator()
    private val statCalculator = StatCalculator()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    private val xpManager = XpManager(repository, xpCalculator)
    private val levelUpManager = LevelUpManager(repository, xpCalculator, statCalculator)

    fun getRequiredXpForNextLevel(currentLevel: Int): Int =
        xpCalculator.getRequiredXpForNextLevel(currentLevel)

    fun getRemainingXp(currentLevel: Int, currentXp: Int): Int =
        xpCalculator.getRemainingXp(currentLevel, currentXp)

    fun addTestXp() {
        coroutineScope.launch {
            xpManager.addTestXp()
        }
    }

    fun tryLevelUp() {
        coroutineScope.launch {
            levelUpManager.tryLevelUp()
        }
    }
}