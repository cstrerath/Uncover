package com.github.cstrerath.uncover

import android.util.Log
import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.dao.GameCharacterDao
import com.github.cstrerath.uncover.data.database.entities.CharacterClass
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.data.database.entities.QuestStage
import java.util.UUID

class PlayerCharacterCreator(
    private val characterDao: GameCharacterDao,
    private val characterQuestProgressDao: CharacterQuestProgressDao
) {
    private val TAG = "PlayerCharacterCreator"

    suspend fun createPlayerCharacter(name: String, characterClass: CharacterClass): GameCharacter {
        val character = when(characterClass) {
            CharacterClass.WARRIOR -> GameCharacter(
                id = UUID.randomUUID().toString(),
                name = name,
                level = 1,
                experience = 0,
                health = 100,
                mana = 50,
                stamina = 100,
                characterClass = characterClass,
                isPlayer = true
            )
            CharacterClass.MAGE -> GameCharacter(
                id = UUID.randomUUID().toString(),
                name = name,
                level = 1,
                experience = 0,
                health = 70,
                mana = 120,
                stamina = 60,
                characterClass = characterClass,
                isPlayer = true
            )
            CharacterClass.THIEF -> GameCharacter(
                id = UUID.randomUUID().toString(),
                name = name,
                level = 1,
                experience = 0,
                health = 80,
                mana = 70,
                stamina = 90,
                characterClass = characterClass,
                isPlayer = true
            )
        }

        try {
            characterDao.insertCharacter(character)
            initializeQuestProgress(character.id)
            logCharacterCreation(character)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create character: ${e.message}", e)
            throw Exception("Failed to create character: ${e.message}")
        }

        return character
    }

    private suspend fun initializeQuestProgress(characterId: String) {
        val initialQuest = CharacterQuestProgress(characterId, 1, QuestStage.NOT_STARTED)
        characterQuestProgressDao.insertProgress(initialQuest)
    }

    private suspend fun logCharacterCreation(character: GameCharacter) {
        val questProgress = characterQuestProgressDao.getCharacterProgress(character.id)
        Log.i(TAG, """
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
        """.trimIndent())
    }
}