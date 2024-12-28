package com.github.cstrerath.uncover.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.github.cstrerath.uncover.data.database.entities.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations WHERE id = :id")
    suspend fun getLocation(id: Int): Location?

    @Query("SELECT * FROM locations")
    suspend fun getAllLocations(): List<Location>

    @Insert
    suspend fun addLocation(location: Location)

    @Update
    suspend fun updateLocation(location: Location)

    @Delete
    suspend fun deleteLocation(location: Location)
}