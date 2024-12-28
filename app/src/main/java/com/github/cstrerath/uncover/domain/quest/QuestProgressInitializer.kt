package com.github.cstrerath.uncover.domain.quest

import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.QuestStage

class QuestProgressInitializer(
    private val characterQuestProgressDao: CharacterQuestProgressDao
) {
    suspend fun initializeQuestProgress(characterId: String) {
        val initialQuest = CharacterQuestProgress(
            characterId = characterId,
            questId = INITIAL_QUEST_ID,
            stage = QuestStage.NOT_STARTED
        )
        characterQuestProgressDao.insertProgress(initialQuest)
    }

    companion object {
        private const val INITIAL_QUEST_ID = 1
    }
}