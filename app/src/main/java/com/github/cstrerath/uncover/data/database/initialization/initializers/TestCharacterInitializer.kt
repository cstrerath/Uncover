package com.github.cstrerath.uncover.data.database.initialization.initializers

import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.initialization.data.CharacterData

class TestCharacterInitializer(database: AppDatabase) {
    private val characterDao = database.gameCharacterDao()

    suspend fun initialize() {
        CharacterData.getAllCharacters().forEach { character ->
            characterDao.insertCharacter(character)
        }
    }
}
