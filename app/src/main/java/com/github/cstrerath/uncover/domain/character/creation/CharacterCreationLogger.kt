package com.github.cstrerath.uncover.domain.character.creation

import android.util.Log
import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.GameCharacter

class CharacterCreationLogger(private val characterQuestProgressDao: CharacterQuestProgressDao) {
    suspend fun logCharacterCreation(character: GameCharacter) {
        val questProgress = characterQuestProgressDao.getCharacterProgress(character.id)
        Log.i("CharacterCreation", buildCharacterCreationLog(character, questProgress))
    }

    private fun buildCharacterCreationLog(
        character: GameCharacter,
        questProgress: List<CharacterQuestProgress>
    ): String = """
        Character created successfully:
        ID: ${character.id}
        Name: ${character.name}
        Class: ${character.characterClass}
        Level: ${character.level}
        Health: ${character.health}
        Mana: ${character.mana}
        Stamina: ${character.stamina}
        
        Quest Progress:
        ${questProgress.joinToString("\n") { "Quest ID: ${it.questId}, Stage: ${it.stage}" }}
    """.trimIndent()
}