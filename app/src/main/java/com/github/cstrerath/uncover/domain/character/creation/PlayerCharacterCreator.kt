package com.github.cstrerath.uncover.domain.character.creation

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.data.database.dao.GameCharacterDao
import com.github.cstrerath.uncover.data.database.entities.CharacterClass
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.domain.character.models.CharacterStatsProvider
import com.github.cstrerath.uncover.domain.quest.mainquest.QuestProgressInitializer
import com.github.cstrerath.uncover.domain.quest.randquest.generators.RandQuestGenerator
import java.util.UUID

class PlayerCharacterCreator(
    private val context: Context,
    private val characterDao: GameCharacterDao,
    private val questProgressInitializer: QuestProgressInitializer,
    private val statsProvider: CharacterStatsProvider,
    private val logger: CharacterCreationLogger
) {
    private val tag = "PlayerCreator"

    suspend fun createPlayerCharacter(name: String, characterClass: CharacterClass): GameCharacter {
        Log.d(tag, "Creating new player character: $name ($characterClass)")
        val character = createCharacterByClass(name, characterClass)
        return saveCharacter(character)
    }

    private fun createCharacterByClass(name: String, characterClass: CharacterClass): GameCharacter {
        Log.d(tag, "Calculating initial stats for $characterClass")
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
        Log.d(tag, "Saving character and initializing quests")
        try {
            val randQuestGenerator = RandQuestGenerator(context)
            randQuestGenerator.generateQuest(INITIAL_RAND_QUEST_ID)

            characterDao.insertCharacter(character)
            Log.d(tag, "Character saved to database")

            questProgressInitializer.initializeMainQuestProgress(character.id)
            questProgressInitializer.initializeRandQuestProgress(character.id)
            Log.d(tag, "Quest progress initialized")

            logger.logCharacterCreation(character)
            return character
        } catch (e: Exception) {
            Log.e(tag, "Character creation failed: ${e.message}")
            throw CharacterCreationException("Failed to create character", e)
        }
    }

    companion object {
        private const val INITIAL_LEVEL = 1
        private const val INITIAL_EXPERIENCE = 0
        private const val INITIAL_RAND_QUEST_ID = 11
    }
}