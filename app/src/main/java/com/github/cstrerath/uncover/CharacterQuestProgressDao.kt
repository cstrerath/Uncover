package com.github.cstrerath.uncover

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterQuestProgressDao {
    @Query("SELECT * FROM character_quest_progress WHERE characterId = :characterId")
    fun getCharacterProgress(characterId: String): List<CharacterQuestProgress>

    @Query("SELECT * FROM character_quest_progress WHERE characterId = :characterId AND questId = :questId")
    fun getQuestProgress(characterId: String, questId: Int): CharacterQuestProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateProgress(progress: CharacterQuestProgress)
}
