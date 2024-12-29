package com.github.cstrerath.uncover.domain.character.progression

import android.util.Log
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.calculator.XpCalculator

class XpManager(
    private val repository: CharacterRepository,
    private val xpCalculator: XpCalculator
) {
    companion object {
        private const val TEST_XP_AMOUNT = 250
        private const val TAG = "XpManager"
    }

    suspend fun addTestXp() {
        try {
            updatePlayerXp()
        } catch (e: Exception) {
            Log.e(TAG, "Error adding XP", e)
        }
    }

    private suspend fun updatePlayerXp() {
        val player = repository.getPlayer() ?: return
        val updatedPlayer = player.copy(experience = player.experience + TEST_XP_AMOUNT)

        repository.updateCharacter(updatedPlayer)
        Log.i(TAG, "XP added: ${player.experience} -> ${updatedPlayer.experience}")
    }
}