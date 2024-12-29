package com.github.cstrerath.uncover.domain.character.models

import com.github.cstrerath.uncover.data.database.entities.CharacterClass

class CharacterStatsProvider {
    data class CharacterStats(val health: Int, val mana: Int, val stamina: Int)

    fun getInitialStats(characterClass: CharacterClass) = when(characterClass) {
        CharacterClass.WARRIOR -> CharacterStats(health = 100, mana = 50, stamina = 100)
        CharacterClass.MAGE -> CharacterStats(health = 70, mana = 120, stamina = 60)
        CharacterClass.THIEF -> CharacterStats(health = 80, mana = 70, stamina = 90)
    }
}