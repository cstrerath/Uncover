package com.github.cstrerath.uncover

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuestDao {
    @Query("SELECT * FROM quests")
    fun getAllQuests(): List<Quest>

    @Query("SELECT * FROM quests WHERE questId = :questId")
    fun getQuestById(questId: Int): Quest

    @Insert
    fun insertQuest(quest: Quest)
}
