package com.github.cstrerath.uncover

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseInitializer(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val DATABASE_INITIALIZED_KEY = "database_initialized"

    suspend fun initializedDatabase() {
        if (!isDatabaseInitialized()) {
            withContext(Dispatchers.IO) {
                val database = AppDatabase.getInstance(context)

                initializeCharacters(database)

                initializeQuests(database)

                setDatabaseInitialized()
            }
        }
    }

    private suspend fun initializeQuests(database: AppDatabase) {
        val questDao = database.questDao()
        val questStepDao = database.questStepDao()

        // Quest 1
        val quest1 = Quest(
            questId = 1,
            questSequence = 1,
            resourceKey = "quest_1",
            startLocationId = 1,  // Startstadt
            questLocationId = 2,  // Mine
            endLocationId = 1     // Zurück zur Stadt
        )
        questDao.insertQuest(quest1)

        // Quest 1 Steps
        val quest1Initial = QuestStep(
            stepId = 1,
            questId = 1,
            stepType = StepType.INITIAL,
            warriorVariantKey = "quest_1_warrior_initial",
            thiefVariantKey = "quest_1_thief_initial",
            mageVariantKey = "quest_1_mage_initial"
        )

        val quest1Solution = QuestStep(
            stepId = 2,
            questId = 1,
            stepType = StepType.SOLUTION,
            warriorVariantKey = "quest_1_warrior_solution",
            thiefVariantKey = "quest_1_thief_solution",
            mageVariantKey = "quest_1_mage_solution"
        )

        val quest1Completion = QuestStep(
            stepId = 3,
            questId = 1,
            stepType = StepType.COMPLETION,
            warriorVariantKey = "quest_1_warrior_completion",
            thiefVariantKey = "quest_1_thief_completion",
            mageVariantKey = "quest_1_mage_completion"
        )

        questStepDao.insertQuestStep(quest1Initial)
        questStepDao.insertQuestStep(quest1Solution)
        questStepDao.insertQuestStep(quest1Completion)
    }

    private suspend fun initializeCharacters(database: AppDatabase) {
        val dao = database.gameCharacterDao()

        val testCharacters = listOf(
            GameCharacter(
                id = "warrior_1",
                name = "Warrior Test",
                level = 1,
                experience = 0,
                health = 100,
                mana = 50,
                stamina = 100,
                characterClass = "Warrior"
            ),
            GameCharacter(
                id = "thief_1",
                name = "Thief Test",
                level = 1,
                experience = 0,
                health = 80,
                mana = 60,
                stamina = 100,
                characterClass = "Thief"
            ),
            GameCharacter(
                id = "mage_1",
                name = "Mage Test",
                level = 1,
                experience = 0,
                health = 70,
                mana = 100,
                stamina = 80,
                characterClass = "Mage"
            )
        )

        testCharacters.forEach { character ->
            dao.insertCharacter(character)
        }
    }

    private fun isDatabaseInitialized(): Boolean {
        return sharedPreferences.getBoolean(DATABASE_INITIALIZED_KEY, false)
    }

    private fun setDatabaseInitialized() {
        sharedPreferences.edit().putBoolean(DATABASE_INITIALIZED_KEY, true).apply()
    }
}