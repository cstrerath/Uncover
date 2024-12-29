package com.github.cstrerath.uncover.domain.character.progression

import android.util.Log
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.calculator.XpCalculator

class XpManager(
    private val repository: CharacterRepository,
    private val xpCalculator: XpCalculator
) {
    companion object {
        private const val TAG = "XpManager"
    }

    suspend fun addXp(xpAmount: Int) {
        try {
            updatePlayerXp(xpAmount)
        } catch (e: Exception) {
            Log.e(TAG, "Error adding XP", e)
        }
    }

    suspend fun addTestXp() = addXp(250)

    private suspend fun updatePlayerXp(xpAmount: Int) {
        val player = repository.getPlayer() ?: return
        val updatedPlayer = player.copy(experience = player.experience + xpAmount)

        repository.updateCharacter(updatedPlayer)
        Log.i(TAG, "XP added: ${player.experience} -> ${updatedPlayer.experience}")
    }
}
