package com.github.cstrerath.uncover.domain.quest.randquest.generators

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.domain.quest.randquest.RandQuest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class RandQuestGenerator(context: Context) {
    private val randNameGenerator = NameGenerator(context)
    private val randActionGenerator = QuestActionGenerator(context)
    private val database = AppDatabase.getInstance(context)
    private val locationDao = database.locationDao()

    suspend fun generateQuest(): Result<RandQuest> {
        return withContext(Dispatchers.IO) {
            try {
                val locations = locationDao.getAllRandQuestLocations()
                if (locations.isEmpty()) {
                    Log.e(TAG, "No locations available for quest generation")
                    return@withContext Result.failure(IllegalStateException("No locations available for quest generation"))
                }

                val randomLocation = locations.random()
                val questText = createQuestText()
                val id = UUID.randomUUID().toString()

                val quest = RandQuest(id, randomLocation, questText)
                Log.i(TAG, "Generated quest: id=$id, location=${randomLocation}, text='$questText'")
                Result.success(quest)
            } catch (e: Exception) {
                Log.e(TAG, "Error generating quest: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    private fun createQuestText(): String {
        val name = randNameGenerator.getRandomName()
        val action = randActionGenerator.getRandomQuestStory()
        return "$name$action"
    }

    companion object {
        private const val TAG = "RandQuestGenerator"
    }
}

