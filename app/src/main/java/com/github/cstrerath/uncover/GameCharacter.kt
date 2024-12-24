package com.github.cstrerath.uncover

import androidx.room.Entity
import androidx.room.PrimaryKey

// Enum für Charakterklassen
enum class CharacterClass(val displayName: String) {
    MAGE("Magier"),
    THIEF("Dieb"),
    WARRIOR("Krieger")
}

@Entity(tableName = "game_characters")
data class GameCharacter(
    @PrimaryKey val id: String,
    val name: String,        // String ist hier völlig ausreichend
    val level: Int,
    val experience: Int,
    val health: Int,
    val mana: Int,
    val stamina: Int,
    val characterClass: CharacterClass,
    val isPlayer: Boolean = false
)



