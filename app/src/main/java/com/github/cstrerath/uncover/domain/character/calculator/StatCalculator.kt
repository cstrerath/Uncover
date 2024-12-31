package com.github.cstrerath.uncover.domain.character.calculator

import android.util.Log
import com.github.cstrerath.uncover.data.database.entities.CharacterClass

class StatCalculator {
    private val tag = "StatCalculator"

    data class StatIncrease(
        val health: Int,
        val stamina: Int,
        val mana: Int
    )

    fun getStatIncreaseForClass(characterClass: CharacterClass): StatIncrease {
        Log.d(tag, "Calculating stat increase for class: $characterClass")
        return when (characterClass) {
            CharacterClass.WARRIOR -> StatIncrease(20, 10, 5)
            CharacterClass.THIEF -> StatIncrease(5, 20, 10)
            CharacterClass.MAGE -> StatIncrease(10, 5, 20)
        }.also {
            Log.d(tag, "Stats calculated: health=${it.health}, stamina=${it.stamina}, mana=${it.mana}")
        }
    }
}