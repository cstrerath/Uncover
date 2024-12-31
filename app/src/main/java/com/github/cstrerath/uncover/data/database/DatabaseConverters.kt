package com.github.cstrerath.uncover.data.database

import android.util.Log
import androidx.room.TypeConverter
import com.github.cstrerath.uncover.data.database.entities.CharacterClass
import com.github.cstrerath.uncover.data.database.entities.QuestStage
import com.github.cstrerath.uncover.data.database.entities.StepType
import com.github.cstrerath.uncover.domain.quest.randquest.RandQuest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DatabaseConverters {
    private val tag = "DatabaseConverters"
    private val gson = Gson()

    @TypeConverter
    fun fromStepType(value: StepType): String {
        Log.v(tag, "Converting StepType to String: $value")
        return value.name
    }

    @TypeConverter
    fun toStepType(value: String): StepType {
        return try {
            StepType.valueOf(value).also {
                Log.v(tag, "Converting String to StepType: $value -> $it")
            }
        } catch (e: IllegalArgumentException) {
            Log.e(tag, "Failed to convert String to StepType: $value", e)
            throw e
        }
    }

    @TypeConverter
    fun fromCharacterClass(value: CharacterClass): String {
        Log.v(tag, "Converting CharacterClass to String: $value")
        return value.name
    }

    @TypeConverter
    fun toCharacterClass(value: String): CharacterClass {
        return try {
            CharacterClass.valueOf(value).also {
                Log.v(tag, "Converting String to CharacterClass: $value -> $it")
            }
        } catch (e: IllegalArgumentException) {
            Log.e(tag, "Failed to convert String to CharacterClass: $value", e)
            throw e
        }
    }

    @TypeConverter
    fun fromQuestStage(value: QuestStage): Int {
        Log.v(tag, "Converting QuestStage to Int: $value -> ${value.ordinal}")
        return value.ordinal
    }

    @TypeConverter
    fun toQuestStage(value: Int): QuestStage {
        return try {
            QuestStage.entries[value].also {
                Log.v(tag, "Converting Int to QuestStage: $value -> $it")
            }
        } catch (e: IndexOutOfBoundsException) {
            Log.e(tag, "Failed to convert Int to QuestStage: $value", e)
            throw e
        }
    }

    @TypeConverter
    fun fromRandQuestDatabase(randQuest: RandQuest): String {
        return try {
            gson.toJson(randQuest).also {
                Log.v(tag, "Converting RandQuest to String: success")
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to convert RandQuest to String", e)
            throw e
        }
    }

    @TypeConverter
    fun toRandQuestDatabase(randQuestString: String): RandQuest {
        return try {
            val type = object : TypeToken<RandQuest>() {}.type
            gson.fromJson<RandQuest>(randQuestString, type).also {
                Log.v(tag, "Converting String to RandQuest: success")
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to convert String to RandQuest", e)
            throw e
        }
    }
}
