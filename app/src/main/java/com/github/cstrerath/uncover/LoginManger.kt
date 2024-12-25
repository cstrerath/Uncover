package com.github.cstrerath.uncover

import android.content.Context

class LoginManager(private val context: Context) {
    suspend fun performInitialCheck(): Boolean {
        // Initialisiere Datenbank
        DatabaseInitializer(context).initializedDatabase()

        // Prüfe ob PlayerCharacter existiert
        val db = AppDatabase.getInstance(context)
        return db.gameCharacterDao().hasPlayerCharacter()
    }
}