// domain/quest/QuestManager.kt
package com.github.cstrerath.uncover.domain.quest.mainquest

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.progression.XpManager

class QuestManager(context: Context) {
    private val tag = "QuestManager"
    private val database = AppDatabase.getInstance(context)
    private val gameCharDao = database.gameCharacterDao()
    private val questProgressDao = database.characterQuestProgressDao()
    private val questDao = database.questDao()

    private val questProgressHandler = QuestProgressHandler(
        progressDao = questProgressDao,
        questDao = questDao,
        xpManager = XpManager(CharacterRepository(context))
    )

    suspend fun processNextQuest() {
        Log.d(tag, "Starting quest processing")
        try {
            val playerId = getPlayerId()
            Log.d(tag, "Found player: $playerId")

            val activeQuest = findActiveQuest(playerId)
            Log.d(tag, "Active quest found: ID=${activeQuest.questId}")

            handleQuestProgress(playerId, activeQuest)
        } catch (e: Exception) {
            Log.e(tag, "Error processing quest: ${e.message}")
            throw e
        }
    }

    private suspend fun getPlayerId(): String =
        gameCharDao.getPlayerCharacterId() ?: run {
            Log.e(tag, "No player found in database")
            throw Exception("No player found")
        }

    private suspend fun findActiveQuest(playerId: String): CharacterQuestProgress =
        questProgressDao.getFirstIncompleteMainQuest(playerId) ?: run {
            Log.w(tag, "No incomplete quests found for player $playerId")
            throw Exception("No incomplete quests found")
        }

    private suspend fun handleQuestProgress(playerId: String, activeQuest: CharacterQuestProgress) {
        Log.d(tag, "Handling progress for quest ${activeQuest.questId}")
        questProgressHandler.handleQuestProgress(playerId, activeQuest.questId)
        logQuestStatus(playerId)
    }

    private suspend fun logQuestStatus(playerId: String) {
        val allQuests = questProgressDao.getCharacterProgress(playerId)
        Log.d(tag, "Current quest status for player $playerId:")
        allQuests.forEach { quest ->
            Log.d(tag, "Quest ${quest.questId}: ${quest.stage}")
        }
    }
}
