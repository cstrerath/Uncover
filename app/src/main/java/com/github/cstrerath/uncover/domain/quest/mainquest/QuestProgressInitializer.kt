package com.github.cstrerath.uncover.domain.quest.mainquest

import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.QuestStage

class QuestProgressInitializer(
    private val characterQuestProgressDao: CharacterQuestProgressDao
) {
    suspend fun initializeMainQuestProgress(characterId: String) {
        val initialMainQuest = CharacterQuestProgress(
            characterId = characterId,
            questId = INITIAL_MAIN_QUEST_ID,
            stage = QuestStage.NOT_STARTED
        )
        characterQuestProgressDao.insertProgress(initialMainQuest)
    }

    suspend fun initializeRandQuestProgress(characterId: String) {
        val initialRandQuest = CharacterQuestProgress(
            characterId = characterId,
            questId = INITIAL_RAND_QUEST_ID,
            stage = QuestStage.NOT_STARTED
        )
        characterQuestProgressDao.insertProgress(initialRandQuest)
    }

    companion object {
        private const val INITIAL_MAIN_QUEST_ID = 1
        private const val INITIAL_RAND_QUEST_ID = 11
    }
}