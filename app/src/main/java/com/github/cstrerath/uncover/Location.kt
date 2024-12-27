package com.github.cstrerath.uncover

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Location(
    @PrimaryKey val id: Int,
    val latitude: Double,
    val longitude: Double
)