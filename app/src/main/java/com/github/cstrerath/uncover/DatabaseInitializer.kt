package com.github.cstrerath.uncover

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
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