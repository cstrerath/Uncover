package com.github.cstrerath.uncover

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_characters")
data class GameCharacter(
    @PrimaryKey val id: String,
    val name: String,
    val level: Int,
    val experience: Int,
    val health: Int,
    val mana: Int,
    val stamina: Int,
    val characterClass: String // "Mage", "Thief", "Warrior"
)


