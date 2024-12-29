package com.github.cstrerath.uncover.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.github.cstrerath.uncover.data.database.entities.GameCharacter

@Dao
interface GameCharacterDao {
    @Insert
    suspend fun insertCharacter(character: GameCharacter)

    @Update
    suspend fun updateCharacter(character: GameCharacter)

    @Query("SELECT * FROM game_characters WHERE id = :id")
    suspend fun getCharacterById(id: String): GameCharacter?

    @Query("SELECT * FROM game_characters WHERE isPlayer = 1 LIMIT 1")
    suspend fun getPlayerCharacter(): GameCharacter?

    @Query("SELECT EXISTS(SELECT 1 FROM game_characters WHERE isPlayer = 1)")
    suspend fun hasPlayerCharacter(): Boolean

    @Query("SELECT id FROM game_characters WHERE isPlayer = 1 LIMIT 1")
    suspend fun getPlayerCharacterId(): String?
}
