package com.github.cstrerath.uncover.domain.managers

import android.content.Context
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.DatabaseInitializer

class LoginManager(private val context: Context) {
    suspend fun performInitialCheck(): Boolean {
        initializeDatabase()
        return checkForExistingPlayer()
    }

    private suspend fun initializeDatabase() {
        DatabaseInitializer(context).initializedDatabase()
    }

    private suspend fun checkForExistingPlayer(): Boolean {
        val database = AppDatabase.getInstance(context)
        return database.gameCharacterDao().hasPlayerCharacter()
    }
}
