package com.github.cstrerath.uncover.ui.viewmodels

import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.dao.GameCharacterDao
import com.github.cstrerath.uncover.data.database.dao.RandQuestDatabaseDao
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.domain.quest.randquest.RandQuestProgressHandler
import com.github.cstrerath.uncover.ui.states.RandQuestUIState
import com.github.cstrerath.uncover.utils.resources.ResourceProvider

class RandQuestViewModel(
    private val resourceProvider: ResourceProvider,
    private val randQuestProgressHandler: RandQuestProgressHandler,
    private val characterDao: GameCharacterDao,
    private val characterProgressDao: CharacterQuestProgressDao,
    private val randQuestDatabaseDao: RandQuestDatabaseDao
) {
    suspend fun loadRandQuestInfo(): RandQuestUIState {
        return try {
            val player = characterDao.getPlayerCharacter() ?: return RandQuestUIState.Error("Player not found")
            val activeQuest = characterProgressDao.getFirstIncompleteRandQuest(player.id)
                ?: return RandQuestUIState.Error("No active random quest")

            val randQuest = randQuestDatabaseDao.getRandQuestById(activeQuest.questId)?.randQuest
                ?: return RandQuestUIState.Error("Random quest not found")

            RandQuestUIState.QuestLoaded(
                questInfo = randQuest.textString,
                quest = activeQuest,
                playerId = player.id,
                location = randQuest.location
            )
        } catch (e: Exception) {
            RandQuestUIState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun progressRandQuest(quest: CharacterQuestProgress) {
        randQuestProgressHandler.handleQuestProgress(quest.characterId, quest.questId)
    }
}
