package com.github.cstrerath.uncover.data.database.initialization.initializers

import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.initialization.data.QuestData
import com.github.cstrerath.uncover.data.database.initialization.data.QuestStepData

class MainQuestInitializer(private val database: AppDatabase) {
    private val questDao = database.questDao()
    private val questStepDao = database.questStepDao()

    suspend fun initialize() {
        initializeQuests()
        initializeQuestSteps()
    }

    private suspend fun initializeQuests() {
        QuestData.getAllQuests().forEach { quest ->
            questDao.insertQuest(quest)
        }
    }

    private suspend fun initializeQuestSteps() {
        QuestStepData.getAllQuestSteps().forEach { step ->
            questStepDao.insertQuestStep(step)
        }
    }
}
