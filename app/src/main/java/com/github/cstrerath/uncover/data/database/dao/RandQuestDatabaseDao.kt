package com.github.cstrerath.uncover.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.cstrerath.uncover.data.database.entities.RandQuestDatabase

@Dao
interface RandQuestDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRandQuest(randQuest: RandQuestDatabase)

    @Query("SELECT * FROM rand_quests WHERE questId = :questId")
    suspend fun getRandQuestById(questId: Int): RandQuestDatabase?

    @Query("SELECT MAX(questId) FROM rand_quests")
    suspend fun getMaxQuestId(): Int?

    @Query("DELETE FROM rand_quests WHERE questId = :questId")
    suspend fun deleteRandQuest(questId: Int)
}
