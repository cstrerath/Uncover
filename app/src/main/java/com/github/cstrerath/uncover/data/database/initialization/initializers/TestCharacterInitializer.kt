package com.github.cstrerath.uncover.data.database.initialization.initializers

import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.initialization.DatabaseInitializer
import com.github.cstrerath.uncover.data.database.initialization.data.CharacterData

class TestCharacterInitializer(database: AppDatabase) : DatabaseInitializer {
    private val characterDao = database.gameCharacterDao()
    private val tag = "CharacterInit"

    override fun getInitializerName() = "Character"

    override suspend fun initialize() {
        Log.d(tag, "Starting character initialization")
        try {
            val characters = CharacterData.getAllCharacters()
            Log.d(tag, "Initializing ${characters.size} characters")

            characters.forEach { character ->
                characterDao.insertCharacter(character)
                Log.v(tag, "Inserted character: ${character.id}")
            }

            Log.d(tag, "Character initialization completed successfully")
        } catch (e: Exception) {
            Log.e(tag, "Failed to initialize characters: ${e.message}")
            throw e
        }
    }
}
