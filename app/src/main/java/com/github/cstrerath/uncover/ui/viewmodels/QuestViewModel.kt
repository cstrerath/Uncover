package com.github.cstrerath.uncover.ui.viewmodels

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
    suspend fun loadQuestInfo(): QuestUIState {
        return try {
            val player = characterDao.getPlayerCharacter() ?: return QuestUIState.Error("Player not found")
            val activeQuest = characterProgressDao.getFirstIncompleteQuest(player.id)
                ?: return QuestUIState.Error("No active quest")

            val questInfo = getQuestInfo(activeQuest, player.characterClass)
            QuestUIState.QuestLoaded(
                questInfo = questInfo,
                quest = activeQuest,
                playerId = player.id
            )
        } catch (e: Exception) {
            QuestUIState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun progressQuest(quest: CharacterQuestProgress) {
        questProgressHandler.handleQuestProgress(quest.characterId, quest.questId)
    }

    private fun getQuestInfo(
        activeQuest: CharacterQuestProgress,
        playerClass: CharacterClass
    ): String {
        val quest = questDao.getQuestById(activeQuest.questId)
        val questStep = getQuestStep(activeQuest)
        val questTextKey = getQuestTextKey(questStep, playerClass)

        return buildQuestText(quest.resourceKey, questTextKey)
    }

    private fun getQuestStep(quest: CharacterQuestProgress) =
        when (quest.stage) {
            QuestStage.AT_START -> questStepDao.getStepForQuestAndType(quest.questId, StepType.INITIAL)
            QuestStage.AT_QUEST_LOCATION -> questStepDao.getStepForQuestAndType(quest.questId, StepType.SOLUTION)
            QuestStage.AT_END -> questStepDao.getStepForQuestAndType(quest.questId, StepType.COMPLETION)
            else -> throw IllegalStateException("Unknown quest stage: ${quest.stage}")
        }

    private fun getQuestTextKey(questStep: QuestStep, playerClass: CharacterClass): String {
        return when (playerClass) {
            CharacterClass.MAGE -> questStep.mageVariantKey
            CharacterClass.THIEF -> questStep.thiefVariantKey
            CharacterClass.WARRIOR -> questStep.warriorVariantKey
        }
    }

    private fun buildQuestText(questKey: String, textKey: String): String {
        val questNameId = QuestResources.getQuestTextId(questKey)
        val questTextId = QuestResources.getQuestTextId(textKey)
        return "${resourceProvider.getString(questNameId)} - ${resourceProvider.getString(questTextId)}"
    }
}