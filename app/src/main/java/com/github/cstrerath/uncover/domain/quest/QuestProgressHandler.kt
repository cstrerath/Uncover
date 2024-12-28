package com.github.cstrerath.uncover.domain.quest

import android.util.Log
import com.github.cstrerath.uncover.ExperienceManager
import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.dao.QuestDao
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.QuestStage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class QuestProgressHandler(
    private val progressDao: CharacterQuestProgressDao,
    private val questDao: QuestDao
) {
    suspend fun handleQuestProgress(characterId: String, questId: Int) {
        val currentProgress = getCurrentProgress(characterId, questId)
        handleExperienceReward(currentProgress.stage)
        val newStage = determineNewStage(currentProgress, questId)
        updateProgress(currentProgress, newStage, characterId)
    }

    private suspend fun getCurrentProgress(characterId: String, questId: Int): CharacterQuestProgress {
        return progressDao.getQuestProgress(characterId, questId)
            ?: CharacterQuestProgress(characterId, questId, QuestStage.NOT_STARTED)
    }

    private fun handleExperienceReward(stage: QuestStage) {
        when (stage) {
            QuestStage.AT_START -> ExperienceManager.addExperience(100)
            QuestStage.AT_QUEST_LOCATION -> ExperienceManager.addExperience(300)
            QuestStage.AT_END -> ExperienceManager.addExperience(600)
            else -> {}
        }
    }

    private fun determineNewStage(currentProgress: CharacterQuestProgress, questId: Int): QuestStage {
        return when {
            isFirstQuestStart(questId, currentProgress) -> QuestStage.AT_START
            canProgressQuest(currentProgress) -> progressToNextStage(currentProgress)
            isFinalQuestCompletion(questId, currentProgress) -> QuestStage.COMPLETED
            else -> currentProgress.stage
        }
    }

    private fun isFirstQuestStart(questId: Int, progress: CharacterQuestProgress): Boolean =
        questId == 1 && progress.stage == QuestStage.NOT_STARTED

    private fun canProgressQuest(progress: CharacterQuestProgress): Boolean =
        progress.stage < QuestStage.AT_END

    private fun progressToNextStage(progress: CharacterQuestProgress): QuestStage =
        QuestStage.entries[progress.stage.ordinal + 1]

    private fun isFinalQuestCompletion(questId: Int, progress: CharacterQuestProgress): Boolean =
        questId == 10 && progress.stage == QuestStage.AT_END

    private suspend fun updateProgress(
        currentProgress: CharacterQuestProgress,
        newStage: QuestStage,
        characterId: String
    ) {
        if (shouldStartNextQuest(currentProgress)) {
            handleNextQuest(currentProgress, characterId)
        } else {
            progressDao.updateProgress(currentProgress.copy(stage = newStage))
        }
    }

    private fun shouldStartNextQuest(progress: CharacterQuestProgress): Boolean =
        progress.stage == QuestStage.AT_END

    private suspend fun handleNextQuest(currentProgress: CharacterQuestProgress, characterId: String) {
        progressDao.updateProgress(currentProgress.copy(stage = QuestStage.COMPLETED))
        val nextQuestId = currentProgress.questId + 1
        progressDao.updateProgress(
            CharacterQuestProgress(
                characterId = characterId,
                questId = nextQuestId,
                stage = QuestStage.AT_START
            )
        )
    }

    suspend fun getActiveQuestLocations(characterId: String): List<Int> = withContext(Dispatchers.IO) {
        val openProgressQuest = progressDao.getFirstIncompleteQuest(characterId)
            ?: return@withContext emptyList()

        try {
            val openQuest = questDao.getQuestById(openProgressQuest.questId)
            return@withContext when (openProgressQuest.stage) {
                QuestStage.AT_START -> listOf(openQuest.startLocationId)
                QuestStage.AT_QUEST_LOCATION -> listOf(openQuest.questLocationId)
                QuestStage.AT_END -> listOf(openQuest.endLocationId)
                else -> emptyList()
            }
        } catch (e: Exception) {
            Log.e("QuestMappingError", "Fehler beim Abrufen der Quest: ${e.message}")
            return@withContext emptyList()
        }
    }
}