package com.github.cstrerath.uncover

import java.util.UUID

class PlayerCharacterCreator(private val characterDao: GameCharacterDao) {
    suspend fun createPlayerCharacter(name: String, characterClass: CharacterClass): GameCharacter {
        val character = when(characterClass) {
            CharacterClass.WARRIOR -> GameCharacter(
                id = UUID.randomUUID().toString(),
                name = name,
                level = 1,
                experience = 0,
                health = 100,
                mana = 50,
                stamina = 100,
                characterClass = characterClass,
                isPlayer = true
            )
            CharacterClass.MAGE -> GameCharacter(
                id = UUID.randomUUID().toString(),
                name = name,
                level = 1,
                experience = 0,
                health = 70,
                mana = 120,
                stamina = 60,
                characterClass = characterClass,
                isPlayer = true
            )
            CharacterClass.THIEF -> GameCharacter(
                id = UUID.randomUUID().toString(),
                name = name,
                level = 1,
                experience = 0,
                health = 80,
                mana = 70,
                stamina = 90,
                characterClass = characterClass,
                isPlayer = true
            )
        }

        characterDao.insertCharacter(character)
        return character
    }
}