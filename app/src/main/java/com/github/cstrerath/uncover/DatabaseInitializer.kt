package com.github.cstrerath.uncover

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseInitializer(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val DATABASE_INITIALIZED_KEY = "database_initialized"

    suspend fun initializedDatabase() {
        if (!isDatabaseInitialized()) {
            withContext(Dispatchers.IO) {
                val database = AppDatabase.getInstance(context)
                val dao = database.gameCharacterDao()

                val initialCharacters = listOf(
                    GameCharacter("1", "Test", 1, 0, 100, 100, 100, "Thief")
                )

                initialCharacters.forEach { character -> dao.insertCharacter(character)}

                setDatabaseInitialized()
            }
        }
    }

    private fun isDatabaseInitialized(): Boolean {
        return sharedPreferences.getBoolean(DATABASE_INITIALIZED_KEY, false)
    }

    private fun setDatabaseInitialized() {
        sharedPreferences.edit().putBoolean(DATABASE_INITIALIZED_KEY, true).apply()
    }
}