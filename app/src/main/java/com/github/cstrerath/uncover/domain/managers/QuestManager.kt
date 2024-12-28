// domain/managers/QuestManager.kt
package com.github.cstrerath.uncover.domain.managers

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.AppDatabase
import com.github.cstrerath.uncover.QuestProgressManager

class QuestManager(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val gameCharDao = database.gameCharacterDao()
    private val questProgressDao = database.characterQuestProgressDao()
    private val questDao = database.questDao()

    suspend fun processNextQuest() {
        try {
            val playerId = getPlayerId()
            val activeQuest = findActiveQuest(playerId)
            updateQuestProgress(playerId, activeQuest.questId)
            logQuestStatus(playerId)
        } catch (e: Exception) {
            Log.e("QuestManager", "Error processing quest: ${e.message}")
        }
    }

    private suspend fun getPlayerId(): String =
        gameCharDao.getPlayerCharacterId() ?: throw Exception("No player found")

    private suspend fun findActiveQuest(playerId: String) =
        questProgressDao.getFirstIncompleteQuest(playerId)
            ?: throw Exception("No incomplete quests found")

    private suspend fun updateQuestProgress(playerId: String, questId: Int) {
        val questProgressManager = QuestProgressManager(questProgressDao, questDao)
        questProgressManager.progressQuest(playerId, questId)
    }

    private suspend fun logQuestStatus(playerId: String) {
        val allQuests = questProgressDao.getCharacterProgress(playerId)
        allQuests.forEach { quest ->
            Log.d("QuestStatus", "Quest ID: ${quest.questId}, Stage: ${quest.stage}")
        }
    }
}
