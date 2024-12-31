package com.github.cstrerath.uncover.ui.viewmodels

import android.util.Log
import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.dao.GameCharacterDao
import com.github.cstrerath.uncover.data.database.dao.RandQuestDatabaseDao
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.domain.quest.randquest.RandQuestProgressHandler
import com.github.cstrerath.uncover.ui.states.RandQuestUIState

class RandQuestViewModel(
    private val randQuestProgressHandler: RandQuestProgressHandler,
    private val characterDao: GameCharacterDao,
    private val characterProgressDao: CharacterQuestProgressDao,
    private val randQuestDatabaseDao: RandQuestDatabaseDao
) {
    companion object {
        private const val TAG = "RandQuestViewModel"
    }

    suspend fun loadRandQuestInfo(): RandQuestUIState {
        Log.d(TAG, "Loading random quest information")
        return try {
            val player = characterDao.getPlayerCharacter() ?: run {
                Log.e(TAG, "Player not found")
                return RandQuestUIState.Error("Player not found")
            }

            val activeQuest = characterProgressDao.getFirstIncompleteRandQuest(player.id) ?: run {
                Log.e(TAG, "No active random quest found for player ${player.id}")
                return RandQuestUIState.Error("No active random quest")
            }

            val randQuest = randQuestDatabaseDao.getRandQuestById(activeQuest.questId)?.randQuest ?: run {
                Log.e(TAG, "Random quest ${activeQuest.questId} not found in database")
                return RandQuestUIState.Error("Random quest not found")
            }

            Log.d(TAG, "Successfully loaded random quest: ${randQuest.textString}")
            RandQuestUIState.QuestLoaded(
                questInfo = randQuest.textString,
                quest = activeQuest,
                playerId = player.id,
                location = randQuest.location
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error loading random quest", e)
            RandQuestUIState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun progressRandQuest(quest: CharacterQuestProgress) {
        Log.d(TAG, "Progressing random quest ${quest.questId} for character ${quest.characterId}")
        try {
            randQuestProgressHandler.handleQuestProgress(quest.characterId, quest.questId)
            Log.d(TAG, "Random quest progress successful")
        } catch (e: Exception) {
            Log.e(TAG, "Error progressing random quest", e)
        }
    }
}

