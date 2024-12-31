package com.github.cstrerath.uncover.domain.quest.mainquest

import android.util.Log
import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.dao.QuestDao
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.Quest
import com.github.cstrerath.uncover.data.database.entities.QuestStage
import com.github.cstrerath.uncover.domain.character.progression.XpManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class QuestProgressHandler(
    private val progressDao: CharacterQuestProgressDao,
    private val questDao: QuestDao,
    private val xpManager: XpManager
) {
    private val tag = "QuestProgress"
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    suspend fun handleQuestProgress(characterId: String, questId: Int) {
        Log.d(tag, "Handling progress for character $characterId, quest $questId")
        try {
            val currentProgress = getCurrentProgress(characterId, questId)
            handleExperienceReward(currentProgress.stage)
            val newStage = determineNewStage(currentProgress, questId)
            updateProgress(currentProgress, newStage, characterId)
            Log.d(tag, "Progress updated: ${currentProgress.stage} -> $newStage")
        } catch (e: Exception) {
            Log.e(tag, "Error handling quest progress: ${e.message}")
            throw e
        }
    }

    private suspend fun getCurrentProgress(characterId: String, questId: Int): CharacterQuestProgress {
        Log.d(tag, "Getting current progress for quest $questId")
        return progressDao.getQuestProgress(characterId, questId)
            ?: CharacterQuestProgress(characterId, questId, QuestStage.NOT_STARTED)
                .also { Log.d(tag, "Created new progress entry") }
    }

    private fun handleExperienceReward(stage: QuestStage) {
        val xpReward = when (stage) {
            QuestStage.AT_START -> XP_REWARD_START
            QuestStage.AT_QUEST_LOCATION -> XP_REWARD_LOCATION
            QuestStage.AT_END -> XP_REWARD_END
            else -> 0
        }

        if (xpReward > 0) {
            Log.d(tag, "Awarding $xpReward XP for stage $stage")
            coroutineScope.launch {
                xpManager.addXp(xpReward)
            }
        }
    }

    private fun determineNewStage(currentProgress: CharacterQuestProgress, questId: Int): QuestStage {
        Log.d(tag, "Determining new stage for quest $questId from ${currentProgress.stage}")
        return when {
            isFirstQuestStart(questId, currentProgress) -> QuestStage.AT_START.also {
                Log.d(tag, "First quest starting")
            }
            canProgressQuest(currentProgress) -> progressToNextStage(currentProgress).also {
                Log.d(tag, "Progressing to next stage: $it")
            }
            isFinalQuestCompletion(questId, currentProgress) -> QuestStage.COMPLETED.also {
                Log.d(tag, "Completing final quest")
            }
            else -> currentProgress.stage.also {
                Log.d(tag, "Maintaining current stage: $it")
            }
        }
    }

    private fun isFirstQuestStart(questId: Int, progress: CharacterQuestProgress): Boolean =
        questId == FIRST_QUEST_ID && progress.stage == QuestStage.NOT_STARTED

    private fun canProgressQuest(progress: CharacterQuestProgress): Boolean =
        progress.stage < QuestStage.AT_END

    private fun progressToNextStage(progress: CharacterQuestProgress): QuestStage =
        QuestStage.entries[progress.stage.ordinal + 1]

    private fun isFinalQuestCompletion(questId: Int, progress: CharacterQuestProgress): Boolean =
        questId == FINAL_QUEST_ID && progress.stage == QuestStage.AT_END

    private suspend fun updateProgress(
        currentProgress: CharacterQuestProgress,
        newStage: QuestStage,
        characterId: String
    ) {
        Log.d(tag, "Updating progress for quest ${currentProgress.questId}")
        if (shouldStartNextQuest(currentProgress) && !isFinalQuest(currentProgress.questId)) {
            handleNextQuest(currentProgress, characterId)
        } else {
            progressDao.updateProgress(currentProgress.copy(stage = newStage))
            Log.d(tag, "Updated quest ${currentProgress.questId} to stage $newStage")
        }
    }

    private fun shouldStartNextQuest(progress: CharacterQuestProgress): Boolean =
        progress.stage == QuestStage.AT_END

    private fun isFinalQuest(questId: Int): Boolean =
        questId == FINAL_QUEST_ID

    private suspend fun handleNextQuest(currentProgress: CharacterQuestProgress, characterId: String) {
        Log.d(tag, "Handling transition to next quest")
        progressDao.updateProgress(currentProgress.copy(stage = QuestStage.COMPLETED))
        Log.d(tag, "Marked quest ${currentProgress.questId} as completed")

        val nextQuestId = (currentProgress.questId + 1).coerceAtMost(FINAL_QUEST_ID)
        val newProgress = CharacterQuestProgress(
            characterId = characterId,
            questId = nextQuestId,
            stage = QuestStage.AT_START
        )
        progressDao.updateProgress(newProgress)
        Log.d(tag, "Started next quest: $nextQuestId")
    }

    suspend fun getActiveQuestLocations(characterId: String): List<Int> {
        Log.d(tag, "Getting active quest locations for character $characterId")
        return withContext(Dispatchers.IO) {
            try {
                val openProgressQuest = progressDao.getFirstIncompleteMainQuest(characterId) ?: run {
                    Log.d(tag, "No incomplete quests found")
                    return@withContext emptyList()
                }

                val openQuest = questDao.getQuestById(openProgressQuest.questId)

                getLocationForStage(openQuest, openProgressQuest.stage).also {
                    Log.d(tag, "Found ${it.size} active locations")
                }
            } catch (e: Exception) {
                Log.e(tag, "Error getting quest locations: ${e.message}")
                emptyList()
            }
        }
    }

    private fun getLocationForStage(quest: Quest, stage: QuestStage): List<Int> =
        when (stage) {
            QuestStage.AT_START -> listOf(quest.startLocationId)
            QuestStage.AT_QUEST_LOCATION -> listOf(quest.questLocationId)
            QuestStage.AT_END -> listOf(quest.endLocationId)
            else -> emptyList()
        }

    companion object {
        private const val XP_REWARD_START = 100
        private const val XP_REWARD_LOCATION = 300
        private const val XP_REWARD_END = 600
        private const val FINAL_QUEST_ID = 10
        private const val FIRST_QUEST_ID = 1
    }
}

