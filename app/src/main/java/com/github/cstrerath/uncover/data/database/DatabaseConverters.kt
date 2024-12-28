package com.github.cstrerath.uncover.data.database

import androidx.room.TypeConverter
import com.github.cstrerath.uncover.data.database.entities.CharacterClass
import com.github.cstrerath.uncover.data.database.entities.QuestStage
import com.github.cstrerath.uncover.data.database.entities.StepType

class DatabaseConverters {
    @TypeConverter
    fun fromStepType(value: StepType): String {
        return value.name
    }

    @TypeConverter
    fun toStepType(value: String): StepType {
        return StepType.valueOf(value)
    }


    @TypeConverter
    fun fromCharacterClass(value: CharacterClass): String {
        return value.name
    }

    @TypeConverter
    fun toCharacterClass(value: String): CharacterClass {
        return CharacterClass.valueOf(value)
    }

    @TypeConverter
    fun fromQuestStage(value: QuestStage): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toQuestStage(value: Int): QuestStage {
        return QuestStage.entries[value]
    }
}
