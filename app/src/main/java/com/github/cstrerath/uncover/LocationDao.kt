package com.github.cstrerath.uncover

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

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