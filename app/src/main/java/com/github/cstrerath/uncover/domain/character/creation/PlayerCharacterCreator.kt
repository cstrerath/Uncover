package com.github.cstrerath.uncover.domain.character.creation

import com.github.cstrerath.uncover.data.database.dao.GameCharacterDao
import com.github.cstrerath.uncover.data.database.entities.CharacterClass
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.domain.character.models.CharacterStatsProvider
import com.github.cstrerath.uncover.domain.quest.QuestProgressInitializer
import java.util.UUID

class PlayerCharacterCreator(
    private val characterDao: GameCharacterDao,
    private val questProgressInitializer: QuestProgressInitializer,
    private val statsProvider: CharacterStatsProvider,
    private val logger: CharacterCreationLogger
) {
    suspend fun createPlayerCharacter(name: String, characterClass: CharacterClass): GameCharacter {
        val character = createCharacterByClass(name, characterClass)
        return saveCharacter(character)
    }

    private fun createCharacterByClass(name: String, characterClass: CharacterClass): GameCharacter {
        val stats = statsProvider.getInitialStats(characterClass)
        return GameCharacter(
            id = UUID.randomUUID().toString(),
            name = name,
            level = INITIAL_LEVEL,
            experience = INITIAL_EXPERIENCE,
            health = stats.health,
            mana = stats.mana,
            stamina = stats.stamina,
            characterClass = characterClass,
            isPlayer = true
        )
    }

    private suspend fun saveCharacter(character: GameCharacter): GameCharacter {
        try {
            characterDao.insertCharacter(character)
            questProgressInitializer.initializeQuestProgress(character.id)
            logger.logCharacterCreation(character)
            return character
        } catch (e: Exception) {
            throw CharacterCreationException("Failed to create character", e)
        }
    }

    companion object {
        private const val INITIAL_LEVEL = 1
        private const val INITIAL_EXPERIENCE = 0
    }
}