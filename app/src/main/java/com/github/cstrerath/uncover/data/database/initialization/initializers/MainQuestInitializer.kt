package com.github.cstrerath.uncover.data.database.initialization.initializers

import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.initialization.data.QuestData
import com.github.cstrerath.uncover.data.database.initialization.data.QuestStepData

class MainQuestInitializer(database: AppDatabase) {
    private val questDao = database.questDao()
    private val questStepDao = database.questStepDao()

    fun initialize() {
        initializeQuests()
        initializeQuestSteps()
    }

    private fun initializeQuests() {
        QuestData.getAllQuests().forEach { quest ->
            questDao.insertQuest(quest)
        }
    }

    private fun initializeQuestSteps() {
        QuestStepData.getAllQuestSteps().forEach { step ->
            questStepDao.insertQuestStep(step)
        }
    }
}
