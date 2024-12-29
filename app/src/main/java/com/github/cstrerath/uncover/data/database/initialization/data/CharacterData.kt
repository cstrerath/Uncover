package com.github.cstrerath.uncover.data.database.initialization.data

import com.github.cstrerath.uncover.data.database.entities.CharacterClass
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import java.util.Locale

object CharacterData {
    private data class CharacterStats(
        val health: Int,
        val mana: Int,
        val stamina: Int
    )

    private val classStats = mapOf(
        CharacterClass.WARRIOR to CharacterStats(100, 50, 100),
        CharacterClass.THIEF to CharacterStats(80, 60, 100),
        CharacterClass.MAGE to CharacterStats(70, 100, 80)
    )

    fun getAllCharacters(): List<GameCharacter> {
        return CharacterClass.entries.map { characterClass ->
            createTestCharacter(characterClass)
        }
    }

    private fun createTestCharacter(characterClass: CharacterClass): GameCharacter {
        val stats = classStats[characterClass]
            ?: throw IllegalStateException("No stats defined for class $characterClass")

        return GameCharacter(
            id = buildCharacterId(characterClass),
            name = buildCharacterName(characterClass),
            level = 1,
            experience = 0,
            health = stats.health,
            mana = stats.mana,
            stamina = stats.stamina,
            characterClass = characterClass,
            isPlayer = false
        )
    }

    private fun buildCharacterId(characterClass: CharacterClass): String {
        return "${characterClass.name.lowercase()}_1"
    }

    private fun buildCharacterName(characterClass: CharacterClass): String {
        val className = characterClass.name.lowercase()
            .replaceFirstChar { it.titlecase(Locale.getDefault()) }
        return "$className Test"
    }
}
