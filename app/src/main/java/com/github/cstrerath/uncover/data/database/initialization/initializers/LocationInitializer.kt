package com.github.cstrerath.uncover.data.database.initialization.initializers

import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.initialization.data.QuestLocationData

class LocationInitializer(private val database: AppDatabase) {
    private val locationDao = database.locationDao()

    suspend fun initialize() {
        Log.d("DatabaseInit", "Starting location initialization")

        QuestLocationData.getAllLocations().forEach { location ->
            locationDao.addLocation(location)
        }

        logInitializationComplete()
    }

    private suspend fun logInitializationComplete() {
        val count = locationDao.getAllLocations().size
        Log.d("DatabaseInit", "Initialization complete. Total locations: $count")
    }
}
