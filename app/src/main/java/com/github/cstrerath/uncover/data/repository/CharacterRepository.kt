package com.github.cstrerath.uncover.data.repository

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.GameCharacter

class CharacterRepository(context: Context) {
    private val gameCharDao = AppDatabase.getInstance(context).gameCharacterDao()

    suspend fun getPlayer(): GameCharacter? =
        gameCharDao.getPlayerCharacter()?.also {
            Log.d(TAG, "Player character loaded: Level ${it.level}")
        } ?: run {
            Log.e(TAG, "No player character found")
            null
        }

    suspend fun updateCharacter(character: GameCharacter) {
        gameCharDao.updateCharacter(character)
    }

    companion object {
        private const val TAG = "CharacterRepository"
    }
}