package com.github.cstrerath.uncover

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.CharacterClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.log10
import kotlin.math.pow

class CharacterProgression(private val context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val gameCharDao = database.gameCharacterDao()
    private val scope = CoroutineScope(Dispatchers.IO + Job())

    private fun calculateRequiredXp(level: Int): Int {
        return when (level) {
            1 -> 0
            2 -> 100
            else -> (1000 * ((level - 1) / 23.0).pow(log10(10.0) / log10(23.0))).toInt()
        }
    }

    fun getRequiredXpForNextLevel(currentLevel: Int): Int {
        return calculateRequiredXp(currentLevel + 1)
    }

    fun getRemainingXp(currentLevel: Int, currentXp: Int): Int {
        return getRequiredXpForNextLevel(currentLevel) - currentXp
    }

    fun addTestXp() {
        scope.launch {
            try {
                val player = gameCharDao.getPlayerCharacter() ?: run {
                    Log.e("CharacterProgression", "Kein Spielercharakter gefunden")
                    return@launch
                }

                val updatedPlayer = player.copy(
                    experience = player.experience + 250
                )

                gameCharDao.updateCharacter(updatedPlayer)
                Log.i("CharacterProgression",
                    "EP hinzugefügt: ${player.experience} -> ${updatedPlayer.experience}")

            } catch (e: Exception) {
                Log.e("CharacterProgression", "Fehler beim Hinzufügen der EP", e)
            }
        }
    }

    fun addQuestXp(experience: Int) {
        scope.launch {
            try {
                val player = gameCharDao.getPlayerCharacter() ?: run {
                    Log.e("CharacterProgression", "Kein Spielercharakter gefunden")
                    return@launch
                }

                val updatedPlayer = player.copy(
                    experience = player.experience + experience
                )

                gameCharDao.updateCharacter(updatedPlayer)
                Log.i("CharacterProgression",
                    "EP hinzugefügt: ${player.experience} -> ${updatedPlayer.experience}")

            } catch (e: Exception) {
                Log.e("CharacterProgression", "Fehler beim Hinzufügen der EP", e)
            }
        }
    }

    fun tryLevelUp() {
        scope.launch {
            try {
                val player = gameCharDao.getPlayerCharacter() ?: run {
                    Log.e("CharacterProgression", "Kein Spielercharakter gefunden")
                    return@launch
                }

                if (player.level >= 25) {
                    Log.i("CharacterProgression", "Maximallevel bereits erreicht")
                    return@launch
                }

                val requiredXp = calculateRequiredXp(player.level + 1)
                if (player.experience < requiredXp) {
                    Log.i("CharacterProgression",
                        "Nicht genügend EP: ${player.experience}/$requiredXp")
                    return@launch
                }

                val updatedPlayer = player.copy(
                    experience = player.experience - requiredXp,
                    level = player.level + 1,
                    health = player.health + when(player.characterClass) {
                        CharacterClass.WARRIOR -> 20
                        CharacterClass.THIEF -> 5
                        CharacterClass.MAGE -> 10
                    },
                    stamina = player.stamina + when(player.characterClass) {
                        CharacterClass.WARRIOR -> 10
                        CharacterClass.THIEF -> 20
                        CharacterClass.MAGE -> 5
                    },
                    mana = player.mana + when(player.characterClass) {
                        CharacterClass.WARRIOR -> 5
                        CharacterClass.THIEF -> 10
                        CharacterClass.MAGE -> 20
                    }
                )

                gameCharDao.updateCharacter(updatedPlayer)
                Log.i("CharacterProgression",
                    "Levelaufstieg erfolgreich: Level ${player.level} -> ${updatedPlayer.level}")

            } catch (e: Exception) {
                Log.e("CharacterProgression", "Fehler beim Levelaufstieg", e)
            }
        }
    }
}
