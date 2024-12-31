package com.github.cstrerath.uncover.domain.quest.randquest.generators

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.Location
import com.github.cstrerath.uncover.data.database.entities.RandQuestDatabase
import com.github.cstrerath.uncover.domain.quest.randquest.RandQuest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RandQuestGenerator(private val context: Context) {
    private val tag = "RandQuestGenerator"
    private val randNameGenerator = NameGenerator(context)
    private val randActionGenerator = QuestActionGenerator(context)
    private val database = AppDatabase.getInstance(context)
    private val locationDao = database.locationDao()
    private val randQuestDatabaseDao = database.randomQuestDatabaseDao()

    suspend fun generateQuest(questId: Int): Result<RandQuest> {
        Log.d(tag, "Starting quest generation for ID: $questId")
        return withContext(Dispatchers.IO) {
            try {
                checkExistingQuest(questId)?.let {
                    return@withContext Result.success(it)
                }

                generateNewQuest(questId)
            } catch (e: Exception) {
                Log.e(tag, "Error generating quest: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    private suspend fun checkExistingQuest(questId: Int): RandQuest? {
        return randQuestDatabaseDao.getRandQuestById(questId)?.let { existingQuest ->
            Log.d(tag, "Found existing quest with ID: $questId")
            existingQuest.randQuest
        }
    }

    private suspend fun generateNewQuest(questId: Int): Result<RandQuest> {
        val locations = locationDao.getAllRandQuestLocations()
        if (locations.isEmpty()) {
            Log.e(tag, "No locations available for quest generation")
            return Result.failure(IllegalStateException(
                context.getString(R.string.error_no_locations_available)
            ))
        }

        val randomLocation = locations.random()
        val questText = createQuestText()

        return saveAndReturnQuest(questId, randomLocation, questText)
    }

    private suspend fun saveAndReturnQuest(
        questId: Int,
        location: Location,
        questText: String
    ): Result<RandQuest> {
        val newQuest = RandQuest(questId, location, questText)
        randQuestDatabaseDao.insertRandQuest(RandQuestDatabase(questId, newQuest))

        Log.i(tag, "Generated new quest:")
        Log.i(tag, "- ID: $questId")
        Log.i(tag, "- Location: ${location.id} at (${location.latitude}, ${location.longitude})")
        Log.v(tag, "- Text: $questText")

        return Result.success(newQuest)
    }

    private fun createQuestText(): String {
        val name = randNameGenerator.getRandomName()
        val action = randActionGenerator.getRandomQuestStory()
        return "$name$action".also {
            Log.d(tag, "Created quest text with name: $name")
        }
    }
}
