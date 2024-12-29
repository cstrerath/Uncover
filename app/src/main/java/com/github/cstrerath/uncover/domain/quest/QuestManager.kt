// domain/quest/QuestManager.kt
package com.github.cstrerath.uncover.domain.quest

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.calculator.XpCalculator
import com.github.cstrerath.uncover.domain.character.progression.XpManager

class QuestManager(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val gameCharDao = database.gameCharacterDao()
    private val questProgressDao = database.characterQuestProgressDao()
    private val questDao = database.questDao()

    private val questProgressHandler = QuestProgressHandler(
        progressDao = questProgressDao,
        questDao = questDao,
        XpManager(CharacterRepository(context), XpCalculator())
    )

    suspend fun processNextQuest() {
        try {
            val playerId = getPlayerId()
            val activeQuest = findActiveQuest(playerId)
            handleQuestProgress(playerId, activeQuest)
        } catch (e: Exception) {
            Log.e("QuestManager", "Error processing quest: ${e.message}")
        }
    }

    suspend fun getActiveQuestLocations(): List<Int> {
        return try {
            val playerId = getPlayerId()
            questProgressHandler.getActiveQuestLocations(playerId)
        } catch (e: Exception) {
            Log.e("QuestManager", "Error getting quest locations: ${e.message}")
            emptyList()
        }
    }

    private suspend fun getPlayerId(): String =
        gameCharDao.getPlayerCharacterId() ?: throw Exception("No player found")

    private suspend fun findActiveQuest(playerId: String): CharacterQuestProgress =
        questProgressDao.getFirstIncompleteQuest(playerId)
            ?: throw Exception("No incomplete quests found")

    private suspend fun handleQuestProgress(playerId: String, activeQuest: CharacterQuestProgress) {
        questProgressHandler.handleQuestProgress(playerId, activeQuest.questId)
        logQuestStatus(playerId)
    }

    private suspend fun logQuestStatus(playerId: String) {
        val allQuests = questProgressDao.getCharacterProgress(playerId)
        allQuests.forEach { quest ->
            Log.d("QuestStatus", "Quest ID: ${quest.questId}, Stage: ${quest.stage}")
        }
    }
}
