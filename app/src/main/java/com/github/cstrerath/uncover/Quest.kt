package com.github.cstrerath.uncover

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quests")
data class Quest(
    @PrimaryKey val questId: Int,
    val questSequence: Int,
    val resourceKey: String,
    val startLocationId: Int,
    val questLocationId: Int,
    val endLocationId: Int
)
