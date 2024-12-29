package com.github.cstrerath.uncover.data.database.entities

import androidx.room.Entity
import androidx.room.TypeConverters
import com.github.cstrerath.uncover.data.database.DatabaseConverters

enum class QuestStage {
    NOT_STARTED,
    AT_START,
    AT_QUEST_LOCATION,
    AT_END,
    COMPLETED
}

@Entity(
    tableName = "character_quest_progress",
    primaryKeys = ["characterId", "questId"]
)
@TypeConverters(DatabaseConverters::class)
data class CharacterQuestProgress(
    val characterId: String,
    val questId: Int,
    val stage: QuestStage
)
