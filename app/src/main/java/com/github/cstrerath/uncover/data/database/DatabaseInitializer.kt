package com.github.cstrerath.uncover.data.database

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.github.cstrerath.uncover.data.database.entities.CharacterClass
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.data.database.entities.Location
import com.github.cstrerath.uncover.data.database.entities.Quest
import com.github.cstrerath.uncover.data.database.entities.QuestStep
import com.github.cstrerath.uncover.data.database.entities.StepType
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

                initializeLocations(database)

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

        // Quest 2
        val quest2 = Quest(
            questId = 2,
            questSequence = 2,
            resourceKey = "quest_2",
            startLocationId = 1,  // Startstadt
            questLocationId = 3,  // Forest
            endLocationId = 1     // Zurück zur Stadt
        )
        questDao.insertQuest(quest2)

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 4,
            questId = 2,
            stepType = StepType.INITIAL,
            warriorVariantKey = "quest_2_warrior_initial",
            thiefVariantKey = "quest_2_thief_initial",
            mageVariantKey = "quest_2_mage_initial"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 5,
            questId = 2,
            stepType = StepType.SOLUTION,
            warriorVariantKey = "quest_2_warrior_solution",
            thiefVariantKey = "quest_2_thief_solution",
            mageVariantKey = "quest_2_mage_solution"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 6,
            questId = 2,
            stepType = StepType.COMPLETION,
            warriorVariantKey = "quest_2_warrior_completion",
            thiefVariantKey = "quest_2_thief_completion",
            mageVariantKey = "quest_2_mage_completion"
        )
        )

        // Quest 3
        val quest3 = Quest(
            questId = 3,
            questSequence = 3,
            resourceKey = "quest_3",
            startLocationId = 1,
            questLocationId = 4, // Cave
            endLocationId = 1
        )
        questDao.insertQuest(quest3)

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 7,
            questId = 3,
            stepType = StepType.INITIAL,
            warriorVariantKey = "quest_3_warrior_initial",
            thiefVariantKey = "quest_3_thief_initial",
            mageVariantKey = "quest_3_mage_initial"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 8,
            questId = 3,
            stepType = StepType.SOLUTION,
            warriorVariantKey = "quest_3_warrior_solution",
            thiefVariantKey = "quest_3_thief_solution",
            mageVariantKey = "quest_3_mage_solution"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 9,
            questId = 3,
            stepType = StepType.COMPLETION,
            warriorVariantKey = "quest_3_warrior_completion",
            thiefVariantKey = "quest_3_thief_completion",
            mageVariantKey = "quest_3_mage_completion"
        )
        )

        // Quest 4
        val quest4= Quest(
            questId=4,
            questSequence=4,
            resourceKey="quest_4",
            startLocationId=1,
            questLocationId=5, // Castle
            endLocationId=1
        )
        questDao.insertQuest(quest4)

        questStepDao.insertQuestStep(
            QuestStep(
            stepId=10,
            questId=4,
            stepType= StepType.INITIAL,
            warriorVariantKey="quest_4_warrior_initial",
            thiefVariantKey="quest_4_thief_initial",
            mageVariantKey="quest_4_mage_initial"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId=11,
            questId=4,
            stepType= StepType.SOLUTION,
            warriorVariantKey="quest_4_warrior_solution",
            thiefVariantKey="quest_4_thief_solution",
            mageVariantKey="quest_4_mage_solution"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId=12,
            questId=4,
            stepType= StepType.COMPLETION,
            warriorVariantKey="quest_4_warrior_completion",
            thiefVariantKey="quest_4_thief_completion",
            mageVariantKey="quest_4_mage_completion"
        )
        )

        // Quest 5
        val quest5 = Quest(
            questId = 5,
            questSequence = 5,
            resourceKey = "quest_5",
            startLocationId = 1,  // Startstadt
            questLocationId = 6,  // Ruins
            endLocationId = 1     // Zurück zur Stadt
        )
        questDao.insertQuest(quest5)

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 13,
            questId = 5,
            stepType = StepType.INITIAL,
            warriorVariantKey = "quest_5_warrior_initial",
            thiefVariantKey = "quest_5_thief_initial",
            mageVariantKey = "quest_5_mage_initial"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 14,
            questId = 5,
            stepType = StepType.SOLUTION,
            warriorVariantKey = "quest_5_warrior_solution",
            thiefVariantKey = "quest_5_thief_solution",
            mageVariantKey = "quest_5_mage_solution"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 15,
            questId = 5,
            stepType = StepType.COMPLETION,
            warriorVariantKey = "quest_5_warrior_completion",
            thiefVariantKey = "quest_5_thief_completion",
            mageVariantKey = "quest_5_mage_completion"
        )
        )

        // Quest 6
        val quest6 = Quest(
            questId = 6,
            questSequence = 6,
            resourceKey = "quest_6",
            startLocationId = 1,
            questLocationId = 7, // Swamp
            endLocationId = 1
        )
        questDao.insertQuest(quest6)

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 16,
            questId = 6,
            stepType = StepType.INITIAL,
            warriorVariantKey = "quest_6_warrior_initial",
            thiefVariantKey = "quest_6_thief_initial",
            mageVariantKey = "quest_6_mage_initial"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 17,
            questId = 6,
            stepType = StepType.SOLUTION,
            warriorVariantKey = "quest_6_warrior_solution",
            thiefVariantKey = "quest_6_thief_solution",
            mageVariantKey = "quest_6_mage_solution"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 18,
            questId = 6,
            stepType = StepType.COMPLETION,
            warriorVariantKey = "quest_6_warrior_completion",
            thiefVariantKey = "quest_6_thief_completion",
            mageVariantKey = "quest_6_mage_completion"
        )
        )

        // Quest 7
        val quest7= Quest(
            questId=7,
            questSequence=7,
            resourceKey="quest_7",
            startLocationId=1,
            questLocationId=8, // Mountain
            endLocationId=1
        )
        questDao.insertQuest(quest7)

        questStepDao.insertQuestStep(
            QuestStep(
            stepId=19,
            questId=7,
            stepType= StepType.INITIAL,
            warriorVariantKey="quest_7_warrior_initial",
            thiefVariantKey="quest_7_thief_initial",
            mageVariantKey="quest_7_mage_initial"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId=20,
            questId=7,
            stepType= StepType.SOLUTION,
            warriorVariantKey="quest_7_warrior_solution",
            thiefVariantKey="quest_7_thief_solution",
            mageVariantKey="quest_7_mage_solution"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId=21,
            questId=7,
            stepType= StepType.COMPLETION,
            warriorVariantKey="quest_7_warrior_completion",
            thiefVariantKey="quest_7_thief_completion",
            mageVariantKey="quest_7_mage_completion"
        )
        )

        val quest8 = Quest(
            questId = 8,
            questSequence = 8,
            resourceKey = "quest_8",
            startLocationId = 1,  // Startstadt
            questLocationId = 9,  // Ancient Ruins
            endLocationId = 1     // Zurück zur Stadt
        )
        questDao.insertQuest(quest8)

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 22,
            questId = 8,
            stepType = StepType.INITIAL,
            warriorVariantKey = "quest_8_warrior_initial",
            thiefVariantKey = "quest_8_thief_initial",
            mageVariantKey = "quest_8_mage_initial"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 23,
            questId = 8,
            stepType = StepType.SOLUTION,
            warriorVariantKey = "quest_8_warrior_solution",
            thiefVariantKey = "quest_8_thief_solution",
            mageVariantKey = "quest_8_mage_solution"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 24,
            questId = 8,
            stepType = StepType.COMPLETION,
            warriorVariantKey = "quest_8_warrior_completion",
            thiefVariantKey = "quest_8_thief_completion",
            mageVariantKey = "quest_8_mage_completion"
        )
        )

        // Quest 9
        val quest9 = Quest(
            questId = 9,
            questSequence = 9,
            resourceKey = "quest_9",
            startLocationId = 1,  // Startstadt
            questLocationId = 10, // Dark Forest
            endLocationId = 1     // Zurück zur Stadt
        )
        questDao.insertQuest(quest9)

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 25,
            questId = 9,
            stepType = StepType.INITIAL,
            warriorVariantKey = "quest_9_warrior_initial",
            thiefVariantKey = "quest_9_thief_initial",
            mageVariantKey = "quest_9_mage_initial"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 26,
            questId = 9,
            stepType = StepType.SOLUTION,
            warriorVariantKey = "quest_9_warrior_solution",
            thiefVariantKey = "quest_9_thief_solution",
            mageVariantKey = "quest_9_mage_solution"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId = 27,
            questId = 9,
            stepType = StepType.COMPLETION,
            warriorVariantKey = "quest_9_warrior_completion",
            thiefVariantKey = "quest_9_thief_completion",
            mageVariantKey = "quest_9_mage_completion"
        )
        )

        // Quest 10
        val quest10= Quest(
            questId=10,
            questSequence=10,
            resourceKey="quest_10",
            startLocationId=1,
            questLocationId=9, // Final Battle Arena
            endLocationId=1
        )
        questDao.insertQuest(quest10)

        questStepDao.insertQuestStep(
            QuestStep(
            stepId=28,
            questId=10,
            stepType= StepType.INITIAL,
            warriorVariantKey="quest_10_warrior_initial",
            thiefVariantKey="quest_10_thief_initial",
            mageVariantKey="quest_10_mage_initial"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId=29,
            questId=10,
            stepType= StepType.SOLUTION,
            warriorVariantKey="quest_10_warrior_solution",
            thiefVariantKey="quest_10_thief_solution",
            mageVariantKey="quest_10_mage_solution"
        )
        )

        questStepDao.insertQuestStep(
            QuestStep(
            stepId=30,
            questId=10,
            stepType= StepType.COMPLETION,
            warriorVariantKey="quest_10_warrior_completion",
            thiefVariantKey="quest_10_thief_completion",
            mageVariantKey="quest_10_mage_completion"
        )
        )

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
                characterClass = CharacterClass.WARRIOR,
                isPlayer = false
            ),
            GameCharacter(
                id = "thief_1",
                name = "Thief Test",
                level = 1,
                experience = 0,
                health = 80,
                mana = 60,
                stamina = 100,
                characterClass = CharacterClass.THIEF,
                isPlayer = false
            ),
            GameCharacter(
                id = "mage_1",
                name = "Mage Test",
                level = 1,
                experience = 0,
                health = 70,
                mana = 100,
                stamina = 80,
                characterClass = CharacterClass.MAGE,
                isPlayer = false
            )
        )

        testCharacters.forEach { character ->
            dao.insertCharacter(character)
        }
    }

    private suspend fun initializeLocations(database: AppDatabase) {
        val locationDao = database.locationDao()

        Log.d("DatabaseInit", "Starting location initialization")


        val questLocations = listOf(
            Location(1, 49.47433, 8.53472),  // DHBW Mannheim
            Location(2, 49.49715, 8.43306),  // BASF-Tor
            Location(3, 49.47734, 8.43439),  // Ludwigshafen Hauptbahnhof
            Location(4, 49.47377, 8.51435),  // Carl-Benz-Stadion
            Location(5, 49.48382, 8.47645),  // Wasserturm
            Location(6, 49.48327, 8.47827),  // Rosengarten
            Location(7, 49.48475, 8.47375),  // Kunsthalle
            Location(8, 49.48361, 8.46023),  // Mannheimer Schloss
            Location(9, 49.48696, 8.47824),  // Neckarwiese
            Location(10, 49.48379, 8.46296)  // Uni Mannheim
        )

        questLocations.forEach { location ->
            locationDao.addLocation(location)
        }

        val count = locationDao.getAllLocations().size
        Log.d("DatabaseInit", "Initialization complete. Total locations: $count")
    }

    private fun isDatabaseInitialized(): Boolean {
        return sharedPreferences.getBoolean(DATABASE_INITIALIZED_KEY, false)
    }

    private fun setDatabaseInitialized() {
        sharedPreferences.edit().putBoolean(DATABASE_INITIALIZED_KEY, true).apply()
    }
}