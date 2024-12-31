package com.github.cstrerath.uncover.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.cstrerath.uncover.data.database.entities.Quest

@Dao
interface QuestDao {
    @Query("SELECT * FROM quests")
    fun getAllQuests(): List<Quest>

    @Query("SELECT * FROM quests WHERE questId = :questId")
    fun getQuestById(questId: Int): Quest

    @Insert
    fun insertQuest(quest: Quest)

    @Query("SELECT COUNT(*) FROM quests")
    suspend fun getQuestCount(): Int
}
