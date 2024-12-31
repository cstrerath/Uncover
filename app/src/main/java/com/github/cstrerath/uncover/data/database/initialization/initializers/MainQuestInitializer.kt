package com.github.cstrerath.uncover.data.database.initialization.initializers

import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.initialization.DatabaseInitializer
import com.github.cstrerath.uncover.data.database.initialization.data.QuestData
import com.github.cstrerath.uncover.data.database.initialization.data.QuestStepData

class MainQuestInitializer(database: AppDatabase) : DatabaseInitializer {
    private val questDao = database.questDao()
    private val questStepDao = database.questStepDao()
    private val tag = "QuestInit"

    override fun getInitializerName() = "MainQuest"

    override suspend fun initialize() {
        Log.d(tag, "Starting quest initialization")
        try {
            initializeQuests()
            initializeQuestSteps()
            logInitializationComplete()
        } catch (e: Exception) {
            Log.e(tag, "Failed to initialize quests: ${e.message}")
            throw e
        }
    }

    private suspend fun initializeQuests() {
        val quests = QuestData.getAllQuests()
        Log.d(tag, "Initializing ${quests.size} quests")
        quests.forEach { quest ->
            questDao.insertQuest(quest)
            Log.v(tag, "Inserted quest: ${quest.questId}")
        }
    }

    private suspend fun initializeQuestSteps() {
        val steps = QuestStepData.getAllQuestSteps()
        Log.d(tag, "Initializing ${steps.size} quest steps")
        steps.forEach { step ->
            questStepDao.insertQuestStep(step)
            Log.v(tag, "Inserted quest step: ${step.stepId}")
        }
    }

    private suspend fun logInitializationComplete() {
        val questCount = questDao.getQuestCount()
        val stepCount = questStepDao.getStepCount()
        Log.d(tag, "Quest initialization complete. Quests: $questCount, Steps: $stepCount")
    }
}
