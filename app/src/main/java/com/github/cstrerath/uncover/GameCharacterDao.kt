package com.github.cstrerath.uncover

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GameCharacterDao {
    @Query("SELECT * FROM game_characters")
    suspend fun getAllCharacters(): List<GameCharacter>

    @Insert
    suspend fun insertCharacter(character: GameCharacter)

    @Update
    suspend fun updateCharacter(character: GameCharacter)

    @Query("SELECT * FROM game_characters WHERE id = :id")
    suspend fun getCharacterById(id: String): GameCharacter?
}
