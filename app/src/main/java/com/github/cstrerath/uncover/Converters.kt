package com.github.cstrerath.uncover

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStepType(value: StepType): String {
        return value.name
    }

    @TypeConverter
    fun toStepType(value: String): StepType {
        return StepType.valueOf(value)
    }
    @TypeConverter
    fun fromQuestProgress(value: QuestProgress): String {
        return value.name
    }

    @TypeConverter
    fun toQuestProgress(value: String): QuestProgress {
        return QuestProgress.valueOf(value)
    }
}
