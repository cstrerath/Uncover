package com.github.cstrerath.uncover.data.database.initialization.initializers

import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.initialization.data.AchievementData

class AchievementInitializer(database: AppDatabase) {
    private val achievementDao = database.achievementDao()

    suspend fun initialize() {
        val achievements = AchievementData.getInitialAchievements()
        achievements.forEach { achievement ->
            achievementDao.insertAchievement(achievement)
        }
    }
}
