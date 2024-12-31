package com.github.cstrerath.uncover.data.database.initialization.initializers

import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.initialization.DatabaseInitializer
import com.github.cstrerath.uncover.data.database.initialization.data.AchievementData

class AchievementInitializer(database: AppDatabase) : DatabaseInitializer {
    private val achievementDao = database.achievementDao()
    private val tag = "AchievementInit"

    override fun getInitializerName() = "Achievement"

    override suspend fun initialize() {
        Log.d(tag, "Starting achievement initialization")
        try {
            val achievements = AchievementData.getInitialAchievements()
            Log.d(tag, "Loaded ${achievements.size} achievements to initialize")

            achievements.forEach { achievement ->
                achievementDao.insertAchievement(achievement)
                Log.v(tag, "Inserted achievement: ${achievement.id}")
            }

            Log.d(tag, "Achievement initialization completed successfully")
        } catch (e: Exception) {
            Log.e(tag, "Failed to initialize achievements: ${e.message}")
            throw e
        }
    }
}
