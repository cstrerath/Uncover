package com.github.cstrerath.uncover.domain.character.models

import android.util.Log
import com.github.cstrerath.uncover.data.database.entities.CharacterClass

class CharacterStatsProvider {
    private val tag = "CharacterStats"

    data class CharacterStats(
        val health: Int,
        val mana: Int,
        val stamina: Int
    )

    fun getInitialStats(characterClass: CharacterClass): CharacterStats {
        Log.d(tag, "Getting initial stats for class: $characterClass")
        return when(characterClass) {
            CharacterClass.WARRIOR -> CharacterStats(
                health = WARRIOR_HEALTH,
                mana = WARRIOR_MANA,
                stamina = WARRIOR_STAMINA
            )
            CharacterClass.MAGE -> CharacterStats(
                health = MAGE_HEALTH,
                mana = MAGE_MANA,
                stamina = MAGE_STAMINA
            )
            CharacterClass.THIEF -> CharacterStats(
                health = THIEF_HEALTH,
                mana = THIEF_MANA,
                stamina = THIEF_STAMINA
            )
        }.also { stats ->
            Log.d(tag, "Stats generated - HP: ${stats.health}, MP: ${stats.mana}, SP: ${stats.stamina}")
        }
    }

    companion object {
        private const val WARRIOR_HEALTH = 100
        private const val WARRIOR_MANA = 65
        private const val WARRIOR_STAMINA = 135

        private const val MAGE_HEALTH = 80
        private const val MAGE_MANA = 100
        private const val MAGE_STAMINA = 120

        private const val THIEF_HEALTH = 55
        private const val THIEF_MANA = 145
        private const val THIEF_STAMINA = 100
    }
}
