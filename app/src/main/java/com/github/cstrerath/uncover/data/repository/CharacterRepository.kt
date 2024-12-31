package com.github.cstrerath.uncover.data.repository

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.GameCharacter

class CharacterRepository(context: Context) {
    private val gameCharDao = AppDatabase.getInstance(context).gameCharacterDao()

    suspend fun getPlayer(): GameCharacter? {
        Log.d(TAG, "Fetching player character")
        return try {
            gameCharDao.getPlayerCharacter()?.also {
                Log.d(TAG, "Player character loaded successfully: ID=${it.id}, Level=${it.level}")
            } ?: run {
                Log.w(TAG, "No player character found in database")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading player character: ${e.message}")
            null
        }
    }

    suspend fun updateCharacter(character: GameCharacter) {
        Log.d(TAG, "Updating character: ID=${character.id}")
        try {
            gameCharDao.updateCharacter(character)
            Log.d(TAG, "Character updated successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update character: ${e.message}")
            throw e
        }
    }

    companion object {
        private const val TAG = "CharacterRepository"
    }
}
