package com.github.cstrerath.uncover.domain.character.progression

import android.util.Log
import com.github.cstrerath.uncover.data.repository.CharacterRepository

class XpManager(private val repository: CharacterRepository) {
    private val tag = "XpManager"

    suspend fun addXp(xpAmount: Int) {
        Log.d(tag, "Adding $xpAmount XP")
        try {
            updatePlayerXp(xpAmount)
        } catch (e: Exception) {
            Log.e(tag, "Error adding XP: ${e.message}", e)
            throw e
        }
    }

    suspend fun addTestXp() {
        Log.d(tag, "Adding test XP amount: $TEST_XP")
        addXp(TEST_XP)
    }

    private suspend fun updatePlayerXp(xpAmount: Int) {
        val player = repository.getPlayer() ?: run {
            Log.w(tag, "No player found for XP update")
            return
        }

        val updatedPlayer = player.copy(experience = player.experience + xpAmount)
        repository.updateCharacter(updatedPlayer)
        Log.i(tag, "XP updated: ${player.experience} -> ${updatedPlayer.experience}")
    }

    companion object {
        private const val TEST_XP = 250
    }
}