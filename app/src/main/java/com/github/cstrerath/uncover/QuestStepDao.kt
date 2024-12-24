package com.github.cstrerath.uncover

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class StepType {
    INITIAL, SOLUTION, COMPLETION
}

@Entity(tableName = "quest_steps")
data class QuestStep(
    @PrimaryKey val stepId: Int,
    val questId: Int,
    val stepType: StepType,
    val warriorVariantKey: String,
    val thiefVariantKey: String,
    val mageVariantKey: String
)
