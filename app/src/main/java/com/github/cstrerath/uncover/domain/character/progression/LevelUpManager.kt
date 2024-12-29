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
    companion object {
        private const val MAX_LEVEL = 25
        private const val TAG = "LevelUpManager"
    }

    suspend fun tryLevelUp() {
        try {
            performLevelUp()
        } catch (e: Exception) {
            Log.e(TAG, "Error during level up", e)
        }
    }

    private suspend fun performLevelUp() {
        val player = repository.getPlayer() ?: return
        if (!canLevelUp(player)) return

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
        Log.i(TAG, "Level up successful: Level ${player.level} -> ${updatedPlayer.level}")
    }

    private fun canLevelUp(player: GameCharacter): Boolean {
        if (player.level >= MAX_LEVEL) {
            Log.i(TAG, "Maximum level already reached")
            return false
        }

        val requiredXp = xpCalculator.calculateRequiredXp(player.level + 1)
        if (player.experience < requiredXp) {
            Log.i(TAG, "Not enough XP: ${player.experience}/$requiredXp")
            return false
        }
        return true
    }
}