package com.github.cstrerath.uncover.data.database.initialization.initializers

import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.initialization.DatabaseInitializer
import com.github.cstrerath.uncover.data.database.initialization.data.QuestLocationData

class LocationInitializer(database: AppDatabase) : DatabaseInitializer {
    private val locationDao = database.locationDao()
    private val tag = "LocationInit"

    override fun getInitializerName() = "Location"

    override suspend fun initialize() {
        Log.d(tag, "Starting location initialization")
        try {
            QuestLocationData.getAllLocations().forEach { location ->
                locationDao.addLocation(location)
                Log.v(tag, "Added location: ${location.id}")
            }
            logInitializationComplete()
        } catch (e: Exception) {
            Log.e(tag, "Failed to initialize locations: ${e.message}")
            throw e
        }
    }

    private suspend fun logInitializationComplete() {
        val count = locationDao.getAllMainQuestLocations().size
        Log.d(tag, "Location initialization complete. Total locations: $count")
    }
}
