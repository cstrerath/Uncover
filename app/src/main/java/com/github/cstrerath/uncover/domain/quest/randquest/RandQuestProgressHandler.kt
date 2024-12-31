package com.github.cstrerath.uncover.domain.quest.randquest

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.dao.RandQuestDatabaseDao
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.QuestStage
import com.github.cstrerath.uncover.domain.character.progression.XpManager
import com.github.cstrerath.uncover.domain.quest.randquest.generators.RandQuestGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RandQuestProgressHandler(
    context: Context,
    private val progressDao: CharacterQuestProgressDao,
    private val randQuestDatabaseDao: RandQuestDatabaseDao,
    private val xpManager: XpManager
) {
    private val tag = "RandQuestProgress"
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private val randQuestGenerator = RandQuestGenerator(context)

    suspend fun handleQuestProgress(characterId: String, questId: Int) {
        Log.d(tag, "Handling quest progress for character $characterId, quest $questId")
        try {
            val currentProgress = getCurrentProgress(characterId, questId)
            Log.d(tag, "Current progress: $currentProgress")

            when (currentProgress.stage) {
                QuestStage.NOT_STARTED -> {
                    Log.d(tag, "Starting new quest")
                    startQuest(currentProgress)
                }
                QuestStage.AT_QUEST_LOCATION -> {
                    Log.d(tag, "Completing current quest")
                    completeCurrentQuest(currentProgress)
                }
                else -> Log.d(tag, "No action needed for stage: ${currentProgress.stage}")
            }
        } catch (e: Exception) {
            Log.e(tag, "Error handling quest progress: ${e.message}")
            throw e
        }
    }

    private suspend fun getCurrentProgress(characterId: String, questId: Int): CharacterQuestProgress {
        Log.d(tag, "Getting progress for quest $questId")
        return progressDao.getQuestProgress(characterId, questId)
            ?: CharacterQuestProgress(characterId, questId, QuestStage.NOT_STARTED)
                .also { Log.d(tag, "Created new progress entry") }
    }

    private suspend fun startQuest(progress: CharacterQuestProgress) {
        Log.d(tag, "Starting quest ${progress.questId}")
        progressDao.updateProgress(progress.copy(stage = QuestStage.AT_QUEST_LOCATION))
        Log.d(tag, "Quest ${progress.questId} started")
    }

    private suspend fun completeCurrentQuest(progress: CharacterQuestProgress) {
        Log.d(tag, "Completing quest ${progress.questId}")
        progressDao.updateProgress(progress.copy(stage = QuestStage.COMPLETED))
        handleExperienceReward(progress.questId)
        startNextQuest(progress.characterId, progress.questId + 1)
    }

    private fun handleExperienceReward(questId: Int) {
        val xpReward = calculateXpReward(questId)
        Log.d(tag, "Awarding $xpReward XP for quest $questId")
        coroutineScope.launch {
            xpManager.addXp(xpReward)
        }
    }

    private fun calculateXpReward(questId: Int): Int {
        return (BASE_XP + kotlin.math.sqrt(questId.toDouble() * XP_MULTIPLIER)).toInt()
    }

    private suspend fun startNextQuest(characterId: String, nextQuestId: Int): Boolean {
        Log.d(tag, "Generating next quest: ID $nextQuestId")
        val newQuest = randQuestGenerator.generateQuest(nextQuestId)

        if (newQuest.isSuccess) {
            delay(QUEST_GENERATION_DELAY)
            return saveAndStartNewQuest(characterId, nextQuestId)
        } else {
            Log.e(tag, "Failed to generate next quest: ID $nextQuestId")
            return false
        }
    }

    private suspend fun saveAndStartNewQuest(characterId: String, questId: Int): Boolean {
        val savedQuest = randQuestDatabaseDao.getRandQuestById(questId)
        return if (savedQuest != null) {
            progressDao.updateProgress(
                CharacterQuestProgress(
                    characterId = characterId,
                    questId = questId,
                    stage = QuestStage.AT_QUEST_LOCATION
                )
            )
            Log.d(tag, "New quest started: ID $questId")
            true
        } else {
            Log.e(tag, "Generated quest not found in database: ID $questId")
            false
        }
    }

    suspend fun getActiveRandQuestLocation(characterId: String): Int? =
        withContext(Dispatchers.IO) {
            try {
                Log.d(tag, "Getting active quest location for character $characterId")
                val openProgressQuest = progressDao.getFirstIncompleteRandQuest(characterId)
                    ?: return@withContext null.also {
                        Log.d(tag, "No incomplete random quest found")
                    }

                randQuestDatabaseDao.getRandQuestById(openProgressQuest.questId)
                    ?.randQuest?.location?.id
                    ?.also { Log.d(tag, "Found active quest location: $it") }
            } catch (e: Exception) {
                Log.e(tag, "Error looking up random quest: ${e.message}")
                null
            }
        }

    companion object {
        private const val BASE_XP = 200
        private const val XP_MULTIPLIER = 25
        private const val QUEST_GENERATION_DELAY = 100L
    }
}