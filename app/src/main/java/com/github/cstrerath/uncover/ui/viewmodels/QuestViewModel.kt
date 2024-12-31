package com.github.cstrerath.uncover.ui.viewmodels

import android.util.Log
import com.github.cstrerath.uncover.resources.QuestResources
import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.dao.GameCharacterDao
import com.github.cstrerath.uncover.data.database.dao.QuestDao
import com.github.cstrerath.uncover.data.database.dao.QuestStepDao
import com.github.cstrerath.uncover.data.database.entities.CharacterClass
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.QuestStage
import com.github.cstrerath.uncover.data.database.entities.QuestStep
import com.github.cstrerath.uncover.data.database.entities.StepType
import com.github.cstrerath.uncover.domain.quest.mainquest.QuestProgressHandler
import com.github.cstrerath.uncover.ui.states.QuestUIState
import com.github.cstrerath.uncover.utils.resources.ResourceProvider

class QuestViewModel(
    private val resourceProvider: ResourceProvider,
    private val questProgressHandler: QuestProgressHandler,
    private val questDao: QuestDao,
    private val questStepDao: QuestStepDao,
    private val characterDao: GameCharacterDao,
    private val characterProgressDao: CharacterQuestProgressDao
) {
    companion object {
        private const val TAG = "QuestViewModel"
    }

    suspend fun loadQuestInfo(): QuestUIState {
        Log.d(TAG, "Loading quest information")
        return try {
            val player = characterDao.getPlayerCharacter() ?: run {
                Log.e(TAG, "Player not found")
                return QuestUIState.Error("Player not found")
            }

            val activeQuest = characterProgressDao.getFirstIncompleteMainQuest(player.id) ?: run {
                Log.e(TAG, "No active quest found for player ${player.id}")
                return QuestUIState.Error("No active quest")
            }

            Log.d(TAG, "Found active quest ${activeQuest.questId} for player ${player.id}")
            val (title, description) = getQuestInfo(activeQuest, player.characterClass)

            QuestUIState.QuestLoaded(
                questTitle = title,
                questInfo = description,
                quest = activeQuest,
                playerId = player.id
            ).also { Log.d(TAG, "Quest loaded successfully: $title") }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading quest", e)
            QuestUIState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun progressQuest(quest: CharacterQuestProgress) {
        Log.d(TAG, "Progressing quest ${quest.questId} for character ${quest.characterId}")
        try {
            questProgressHandler.handleQuestProgress(quest.characterId, quest.questId)
            Log.d(TAG, "Quest progress successful")
        } catch (e: Exception) {
            Log.e(TAG, "Error progressing quest", e)
        }
    }

    private fun getQuestInfo(
        activeQuest: CharacterQuestProgress,
        playerClass: CharacterClass
    ): Pair<String, String> {
        val quest = questDao.getQuestById(activeQuest.questId)
        val questStep = getQuestStep(activeQuest)
        val questTextKey = getQuestTextKey(questStep, playerClass)
        Log.d(TAG, "Getting quest info for quest ${quest.resourceKey}, stage ${activeQuest.stage}")
        return buildQuestText(quest.resourceKey, questTextKey)
    }

    private fun getQuestStep(quest: CharacterQuestProgress): QuestStep {
        Log.d(TAG, "Getting quest step for stage: ${quest.stage}")
        return when (quest.stage) {
            QuestStage.AT_START -> questStepDao.getStepForQuestAndType(quest.questId, StepType.INITIAL)
            QuestStage.AT_QUEST_LOCATION -> questStepDao.getStepForQuestAndType(quest.questId, StepType.SOLUTION)
            QuestStage.AT_END -> questStepDao.getStepForQuestAndType(quest.questId, StepType.COMPLETION)
            else -> throw IllegalStateException("Unknown quest stage: ${quest.stage}")
        }
    }

    private fun getQuestTextKey(questStep: QuestStep, playerClass: CharacterClass): String {
        Log.d(TAG, "Getting text key for class: $playerClass")
        return when (playerClass) {
            CharacterClass.MAGE -> questStep.mageVariantKey
            CharacterClass.THIEF -> questStep.thiefVariantKey
            CharacterClass.WARRIOR -> questStep.warriorVariantKey
        }
    }

    private fun buildQuestText(questKey: String, textKey: String): Pair<String, String> {
        val questNameId = QuestResources.getQuestTextId(questKey)
        val questTextId = QuestResources.getQuestTextId(textKey)
        return Pair(
            resourceProvider.getString(questNameId),
            resourceProvider.getString(questTextId)
        ).also { (title, _) ->
            Log.d(TAG, "Built quest text with title: $title")
        }
    }
}
