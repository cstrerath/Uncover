package com.github.cstrerath.uncover.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress

@Dao
interface CharacterQuestProgressDao {
    @Query("SELECT * FROM character_quest_progress WHERE characterId = :characterId")
    suspend fun getCharacterProgress(characterId: String): List<CharacterQuestProgress>

    @Query("SELECT * FROM character_quest_progress WHERE characterId = :characterId AND questId = :questId")
    suspend fun getQuestProgress(characterId: String, questId: Int): CharacterQuestProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProgress(progress: CharacterQuestProgress)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProgress(progress: CharacterQuestProgress)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMultipleProgress(progressList: List<CharacterQuestProgress>)

    @Query("SELECT * FROM character_quest_progress WHERE characterId = :characterId AND questId <= 10 ORDER BY questId DESC LIMIT 1")
    suspend fun getFirstIncompleteMainQuest(characterId: String): CharacterQuestProgress?

    @Query("SELECT * FROM character_quest_progress WHERE characterId = :characterId AND questId > 10 ORDER BY questId DESC LIMIT 1")
    suspend fun getFirstIncompleteRandQuest(characterId: String): CharacterQuestProgress?
}
