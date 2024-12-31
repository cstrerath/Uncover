package com.github.cstrerath.uncover.domain.character.progression

import android.util.Log
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.calculator.StatCalculator
import com.github.cstrerath.uncover.domain.character.calculator.XpCalculator

class LevelUpManager(
    private val repository: CharacterRepository,
    private val xpCalculator: XpCalculator,
    private val statCalculator: StatCalculator
) {
    private val tag = "LevelUpManager"

    suspend fun tryLevelUp() {
        Log.d(tag, "Starting level up process")
        try {
            performLevelUp()
        } catch (e: Exception) {
            Log.e(tag, "Error during level up: ${e.message}", e)
            throw e
        }
    }

    private suspend fun performLevelUp() {
        val player = repository.getPlayer() ?: run {
            Log.w(tag, "No player found for level up")
            return
        }

        if (!canLevelUp(player)) {
            Log.d(tag, "Level up conditions not met")
            return
        }

        val requiredXp = xpCalculator.calculateRequiredXp(player.level + 1)
        val statIncrease = statCalculator.getStatIncreaseForClass(player.characterClass)

        val updatedPlayer = player.copy(
            experience = player.experience - requiredXp,
            level = player.level + 1,
            health = player.health + statIncrease.health,
            stamina = player.stamina + statIncrease.stamina,
            mana = player.mana + statIncrease.mana
        )

        repository.updateCharacter(updatedPlayer)
        Log.i(tag, "Level up successful: ${player.level} -> ${updatedPlayer.level}")
        Log.d(tag, "New stats - HP: ${updatedPlayer.health}, MP: ${updatedPlayer.mana}, SP: ${updatedPlayer.stamina}")
    }

    private fun canLevelUp(player: GameCharacter): Boolean {
        if (player.level >= MAX_LEVEL) {
            Log.i(tag, "Maximum level ($MAX_LEVEL) already reached")
            return false
        }

        val requiredXp = xpCalculator.calculateRequiredXp(player.level + 1)
        return if (player.experience >= requiredXp) {
            Log.d(tag, "Level up possible - XP: ${player.experience}/$requiredXp")
            true
        } else {
            Log.d(tag, "Insufficient XP for level up: ${player.experience}/$requiredXp")
            false
        }
    }

    companion object {
        private const val MAX_LEVEL = 25
    }
}