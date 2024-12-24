package com.github.cstrerath.uncover

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters

@Database(entities = [
    GameCharacter::class,
    Quest::class,
    QuestStep::class,
    CharacterQuestProgress::class
                     ],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameCharacterDao(): GameCharacterDao
    abstract fun questDao(): QuestDao
    abstract fun questStepDao(): QuestStepDao
    abstract fun characterQuestProgressDao(): CharacterQuestProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}