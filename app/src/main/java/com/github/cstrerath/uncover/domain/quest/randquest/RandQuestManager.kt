package com.github.cstrerath.uncover.domain.quest.randquest

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.progression.XpManager

class RandQuestManager(context: Context) {
    private val tag = "RandQuestManager"
    private val database = AppDatabase.getInstance(context)
    private val gameCharDao = database.gameCharacterDao()
    private val questProgressDao = database.characterQuestProgressDao()
    private val randQuestDatabaseDao = database.randomQuestDatabaseDao()

    private val randQuestProgressHandler = RandQuestProgressHandler(
        context = context,
        progressDao = questProgressDao,
        randQuestDatabaseDao = randQuestDatabaseDao,
        xpManager = XpManager(CharacterRepository(context))
    )

    suspend fun processNextRandQuest() {
        Log.d(tag, "Starting random quest processing")
        try {
            val playerId = getPlayerId()
            Log.d(tag, "Found player: $playerId")

            val activeQuest = findActiveRandQuest(playerId)
            Log.d(tag, "Found active quest: ${activeQuest.questId}")

            handleRandQuestProgress(playerId, activeQuest)
            Log.d(tag, "Quest processing completed successfully")
        } catch (e: Exception) {
            Log.e(tag, "Error processing random quest: ${e.message}")
            throw e
        }
    }

    private suspend fun getPlayerId(): String =
        gameCharDao.getPlayerCharacterId() ?: run {
            Log.e(tag, "No player character found in database")
            throw Exception("No player found")
        }

    private suspend fun findActiveRandQuest(playerId: String): CharacterQuestProgress =
        questProgressDao.getFirstIncompleteRandQuest(playerId) ?: run {
            Log.w(tag, "No incomplete random quests found for player $playerId")
            throw Exception("No incomplete random quests found")
        }

    private suspend fun handleRandQuestProgress(
        playerId: String,
        activeQuest: CharacterQuestProgress
    ) {
        Log.d(tag, "Handling progress for quest ${activeQuest.questId}")
        randQuestProgressHandler.handleQuestProgress(playerId, activeQuest.questId)
        logRandQuestStatus(playerId)
    }

    private suspend fun logRandQuestStatus(playerId: String) {
        val allRandQuests = questProgressDao.getCharacterProgress(playerId)
        Log.d(tag, "Current random quest status for player $playerId:")
        allRandQuests.forEach { quest ->
            Log.d(tag, "Quest ${quest.questId}: ${quest.stage}")
        }
    }
}

