package com.github.cstrerath.uncover

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ExperienceManager {
    private lateinit var database: AppDatabase
    private val scope = CoroutineScope(Dispatchers.IO)

    fun initialize(context: Context) {
        database = AppDatabase.getInstance(context)
    }

    fun addExperience(xp: Int) {
        scope.launch {
            try {
                val player = database.gameCharacterDao().getPlayerCharacter() ?: run {
                    Log.e("ExperienceManager", "Kein Spielercharakter gefunden")
                    return@launch
                }

                val updatedPlayer = player.copy(
                    experience = player.experience + xp
                )

                database.gameCharacterDao().updateCharacter(updatedPlayer)
                Log.i("ExperienceManager",
                    "EP hinzugefügt: ${player.experience} -> ${updatedPlayer.experience}")

            } catch (e: Exception) {
                Log.e("ExperienceManager", "Fehler beim Hinzufügen der EP", e)
            }
        }
    }
}
