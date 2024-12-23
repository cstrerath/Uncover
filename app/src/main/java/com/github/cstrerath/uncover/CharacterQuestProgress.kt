package com.github.cstrerath.uncover

import androidx.room.Entity
import androidx.room.TypeConverters

enum class QuestProgress {
    NOT_STARTED, INITIAL, SOLUTION, COMPLETION, FINISHED
}

@Entity(
    tableName = "character_quest_progress",
    primaryKeys = ["characterId", "questId"]
)
@TypeConverters(Converters::class)
data class CharacterQuestProgress(
    val characterId: String,
    val questId: Int,
    val currentStep: QuestProgress
)
