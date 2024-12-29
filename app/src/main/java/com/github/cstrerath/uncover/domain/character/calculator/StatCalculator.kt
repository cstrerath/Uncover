package com.github.cstrerath.uncover.domain.character.calculator

import com.github.cstrerath.uncover.data.database.entities.CharacterClass

class StatCalculator {
    data class StatIncrease(
        val health: Int,
        val stamina: Int,
        val mana: Int
    )

    fun getStatIncreaseForClass(characterClass: CharacterClass) = when (characterClass) {
        CharacterClass.WARRIOR -> StatIncrease(20, 10, 5)
        CharacterClass.THIEF -> StatIncrease(5, 20, 10)
        CharacterClass.MAGE -> StatIncrease(10, 5, 20)
    }
}