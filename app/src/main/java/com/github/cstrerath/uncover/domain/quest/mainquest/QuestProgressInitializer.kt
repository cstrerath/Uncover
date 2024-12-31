package com.github.cstrerath.uncover.domain.quest.mainquest

import android.util.Log
import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.QuestStage

class QuestProgressInitializer(
    private val characterQuestProgressDao: CharacterQuestProgressDao
) {
    private val tag = "QuestProgressInit"

    suspend fun initializeMainQuestProgress(characterId: String) {
        Log.d(tag, "Initializing main quest progress for character: $characterId")
        try {
            val initialMainQuest = CharacterQuestProgress(
                characterId = characterId,
                questId = INITIAL_MAIN_QUEST_ID,
                stage = QuestStage.NOT_STARTED
            )
            characterQuestProgressDao.insertProgress(initialMainQuest)
            Log.d(tag, "Main quest progress initialized with quest ID: $INITIAL_MAIN_QUEST_ID")
        } catch (e: Exception) {
            Log.e(tag, "Failed to initialize main quest progress: ${e.message}")
            throw e
        }
    }

    suspend fun initializeRandQuestProgress(characterId: String) {
        Log.d(tag, "Initializing random quest progress for character: $characterId")
        try {
            val initialRandQuest = CharacterQuestProgress(
                characterId = characterId,
                questId = INITIAL_RAND_QUEST_ID,
                stage = QuestStage.NOT_STARTED
            )
            characterQuestProgressDao.insertProgress(initialRandQuest)
            Log.d(tag, "Random quest progress initialized with quest ID: $INITIAL_RAND_QUEST_ID")
        } catch (e: Exception) {
            Log.e(tag, "Failed to initialize random quest progress: ${e.message}")
            throw e
        }
    }

    companion object {
        private const val INITIAL_MAIN_QUEST_ID = 1
        private const val INITIAL_RAND_QUEST_ID = 11
    }
}
