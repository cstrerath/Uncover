package com.github.cstrerath.uncover.domain.character.creation

import android.util.Log
import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.GameCharacter

class CharacterCreationLogger(private val characterQuestProgressDao: CharacterQuestProgressDao) {
    private val tag = "CharacterCreation"

    suspend fun logCharacterCreation(character: GameCharacter) {
        try {
            val questProgress = characterQuestProgressDao.getCharacterProgress(character.id)
            logCharacterDetails(character)
            logQuestProgress(questProgress)
        } catch (e: Exception) {
            Log.e(tag, "Failed to log character creation: ${e.message}")
        }
    }

    private fun logCharacterDetails(character: GameCharacter) {
        Log.i(tag, "Character created - ID: ${character.id}")
        Log.i(tag, "Basic Info - Name: ${character.name}, Class: ${character.characterClass}")
        Log.i(tag, "Stats - Level: ${character.level}, XP: ${character.experience}")
        Log.i(tag, "Attributes - HP: ${character.health}, MP: ${character.mana}, SP: ${character.stamina}")
    }

    private fun logQuestProgress(questProgress: List<CharacterQuestProgress>) {
        Log.i(tag, "Initial Quest Progress (${questProgress.size} quests):")
        questProgress.forEach { progress ->
            Log.i(tag, "Quest ${progress.questId}: ${progress.stage}")
        }
    }
}