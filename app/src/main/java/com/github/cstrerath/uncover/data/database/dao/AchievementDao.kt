package com.github.cstrerath.uncover.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.cstrerath.uncover.data.database.entities.Achievement

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements")
    suspend fun getAllAchievements(): List<Achievement>

    @Query("SELECT * FROM achievements WHERE id = :achievementId")
    suspend fun getAchievementById(achievementId: Int): Achievement?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: Achievement)

    @Query("UPDATE achievements SET reached = :reached WHERE id = :achievementId")
    suspend fun updateAchievementStatus(achievementId: Int, reached: Boolean)

    @Query("SELECT COUNT(*) FROM achievements WHERE reached = 1")
    suspend fun getReachedAchievementsCount(): Int
}