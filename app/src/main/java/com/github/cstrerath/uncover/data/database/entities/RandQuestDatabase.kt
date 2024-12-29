package com.github.cstrerath.uncover.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.cstrerath.uncover.domain.quest.randquest.RandQuest

@Entity(tableName = "rand_quests")
data class RandQuestDatabase(
    @PrimaryKey val questId: Int,
    val randQuest: RandQuest
)
