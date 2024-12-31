package com.github.cstrerath.uncover.data.database.initialization

import android.content.Context
import android.content.SharedPreferences
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.initialization.initializers.AchievementInitializer
import com.github.cstrerath.uncover.data.database.initialization.initializers.LocationInitializer
import com.github.cstrerath.uncover.data.database.initialization.initializers.MainQuestInitializer
import com.github.cstrerath.uncover.data.database.initialization.initializers.TestCharacterInitializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseInitializationManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    suspend fun initializedDatabase() {
        if (!isDatabaseInitialized()) {
            withContext(Dispatchers.IO) {
                initializeAllData()
                setDatabaseInitialized()
            }
        }
    }

    private suspend fun initializeAllData() {
        LocationInitializer(database).initialize()
        TestCharacterInitializer(database).initialize()
        MainQuestInitializer(database).initialize()
        AchievementInitializer(database).initialize()
    }

    private fun isDatabaseInitialized(): Boolean {
        return sharedPreferences.getBoolean(DATABASE_INITIALIZED_KEY, false)
    }

    private fun setDatabaseInitialized() {
        sharedPreferences.edit()
            .putBoolean(DATABASE_INITIALIZED_KEY, true)
            .apply()
    }

    companion object {
        private const val DATABASE_INITIALIZED_KEY = "database_initialized"
    }
}
