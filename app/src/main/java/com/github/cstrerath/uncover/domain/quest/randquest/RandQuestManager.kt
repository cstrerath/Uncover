package com.github.cstrerath.uncover.domain.quest.randquest

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.dao.RandQuestDatabaseDao
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.QuestStage
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.progression.XpManager
import com.github.cstrerath.uncover.domain.quest.randquest.generators.RandQuestGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RandQuestManager(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val gameCharDao = database.gameCharacterDao()
    private val questProgressDao = database.characterQuestProgressDao()
    private val randQuestDatabaseDao = database.randomQuestDatabaseDao()

    private val randQuestProgressHandler = RandQuestProgressHandler(
        context = context,
        progressDao = questProgressDao,
        randQuestDatabaseDao = randQuestDatabaseDao,
        XpManager(CharacterRepository(context))
    )

    suspend fun processNextRandQuest() {
        try {
            val playerId = getPlayerId()
            val activeQuest = findActiveRandQuest(playerId)
            handleRandQuestProgress(playerId, activeQuest)
        } catch (e: Exception) {
            Log.e("RandQuestManager", "Error processing random quest: ${e.message}")
        }
    }

    private suspend fun getPlayerId(): String =
        gameCharDao.getPlayerCharacterId() ?: throw Exception("No player found")

    private suspend fun findActiveRandQuest(playerId: String): CharacterQuestProgress =
        questProgressDao.getFirstIncompleteRandQuest(playerId)
            ?: throw Exception("No incomplete random quests found")

    private suspend fun handleRandQuestProgress(playerId: String, activeQuest: CharacterQuestProgress) {
        randQuestProgressHandler.handleQuestProgress(playerId, activeQuest.questId)
        logRandQuestStatus(playerId)
    }

    private suspend fun logRandQuestStatus(playerId: String) {
        val allRandQuests = questProgressDao.getCharacterProgress(playerId)
        allRandQuests.forEach { quest ->
            Log.d("RandQuestStatus", "Quest ID: ${quest.questId}, Stage: ${quest.stage}")
        }
    }
}

class RandQuestProgressHandler(
    private val context: Context,
    private val progressDao: CharacterQuestProgressDao,
    private val randQuestDatabaseDao: RandQuestDatabaseDao,
    private val xpManager: XpManager
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private val randQuestGenerator = RandQuestGenerator(context)

    suspend fun handleQuestProgress(characterId: String, questId: Int) {
        val currentProgress = getCurrentProgress(characterId, questId)
        Log.d("Debug", currentProgress.toString())
        when (currentProgress.stage) {
            QuestStage.NOT_STARTED -> startQuest(currentProgress)
            QuestStage.AT_QUEST_LOCATION -> completeCurrentQuest(currentProgress)
            else -> {}
        }
    }

    private suspend fun getCurrentProgress(characterId: String, questId: Int): CharacterQuestProgress {
        return progressDao.getQuestProgress(characterId, questId)
            ?: CharacterQuestProgress(characterId, questId, QuestStage.NOT_STARTED)
    }

    private suspend fun startQuest(progress: CharacterQuestProgress) {
        progressDao.updateProgress(progress.copy(stage = QuestStage.AT_QUEST_LOCATION))
    }

    private suspend fun completeCurrentQuest(progress: CharacterQuestProgress) {
        progressDao.updateProgress(progress.copy(stage = QuestStage.COMPLETED))
        handleExperienceReward(progress.questId)
        startNextQuest(progress.characterId, progress.questId + 1)
    }

    private fun handleExperienceReward(questId: Int) {
        coroutineScope.launch {
            val xpReward = calculateXpReward(questId)
            xpManager.addXp(xpReward)
        }
    }

    private fun calculateXpReward(questId: Int): Int {
        return (200 + kotlin.math.sqrt(questId.toDouble() * 25)).toInt()
    }

    private suspend fun startNextQuest(characterId: String, nextQuestId: Int): Boolean {
        val newQuest = randQuestGenerator.generateQuest(nextQuestId)
        if (newQuest.isSuccess) {
            delay(100)

            val savedQuest = randQuestDatabaseDao.getRandQuestById(nextQuestId)
            if (savedQuest != null) {
                progressDao.updateProgress(
                    CharacterQuestProgress(
                        characterId = characterId,
                        questId = nextQuestId,
                        stage = QuestStage.AT_QUEST_LOCATION
                    )
                )
                Log.d("RandQuestProgressHandler", "New quest started: ID $nextQuestId")
                return true
            } else {
                Log.e("RandQuestProgressHandler", "Generated quest not found in database: ID $nextQuestId")
            }
        } else {
            Log.e("RandQuestProgressHandler", "Failed to generate next quest: ID $nextQuestId")
        }
        return false
    }

    suspend fun getActiveRandQuestLocation(characterId: String): Int? = withContext(Dispatchers.IO) {
        val openProgressQuest = progressDao.getFirstIncompleteRandQuest(characterId) ?: return@withContext null
        try {
            val openQuest = randQuestDatabaseDao.getRandQuestById(openProgressQuest.questId)?.randQuest?.location?.id
            return@withContext openQuest
        } catch (e: Exception) {
            Log.e("RandQuestMappingError", "Lookup of random quest not possible: ${e.message}")
            return@withContext null
        }
    }
}
