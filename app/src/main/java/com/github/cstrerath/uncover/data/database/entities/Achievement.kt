package com.github.cstrerath.uncover.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val stringResourceId: Int,
    @ColumnInfo(name = "reached") val reached: Boolean = false
)