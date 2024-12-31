package com.github.cstrerath.uncover.domain.auth

import android.content.Context
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.initialization.DatabaseInitializationManager

class LoginManager(private val context: Context) {
    suspend fun performInitialCheck(): Boolean {
        initializeDatabase()
        return checkForExistingPlayer()
    }

    private suspend fun initializeDatabase() {
        DatabaseInitializationManager(context).initializedDatabase()
    }

    private suspend fun checkForExistingPlayer(): Boolean {
        val database = AppDatabase.getInstance(context)
        return database.gameCharacterDao().hasPlayerCharacter()
    }
}
