package com.github.cstrerath.uncover.domain.auth

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.initialization.DatabaseInitializationManager

class LoginManager(private val context: Context) {
    private val tag = "LoginManager"
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    suspend fun performInitialCheck(): Boolean {
        Log.d(tag, "Starting initial login check")
        return try {
            initializeDatabase()
            val hasPlayer = checkForExistingPlayer()
            Log.d(tag, "Initial check complete. Player exists: $hasPlayer")
            hasPlayer
        } catch (e: Exception) {
            Log.e(tag, "Error during initial check: ${e.message}")
            throw e
        }
    }

    private suspend fun initializeDatabase() {
        Log.d(tag, "Initializing database")
        try {
            DatabaseInitializationManager(context).initializeDatabase()
            Log.d(tag, "Database initialization complete")
        } catch (e: Exception) {
            Log.e(tag, "Database initialization failed: ${e.message}")
            throw e
        }
    }

    private suspend fun checkForExistingPlayer(): Boolean {
        Log.d(tag, "Checking for existing player")
        return try {
            database.gameCharacterDao().hasPlayerCharacter().also { exists ->
                Log.d(tag, "Player check complete. Exists: $exists")
            }
        } catch (e: Exception) {
            Log.e(tag, "Error checking for player: ${e.message}")
            throw e
        }
    }
}
