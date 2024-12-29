package com.github.cstrerath.uncover.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.dao.CharacterQuestProgressDao
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.data.database.dao.GameCharacterDao
import com.github.cstrerath.uncover.data.database.entities.Location
import com.github.cstrerath.uncover.data.database.dao.LocationDao
import com.github.cstrerath.uncover.data.database.entities.Quest
import com.github.cstrerath.uncover.data.database.dao.QuestDao
import com.github.cstrerath.uncover.data.database.entities.QuestStep
import com.github.cstrerath.uncover.data.database.dao.QuestStepDao
import com.github.cstrerath.uncover.data.database.entities.RandQuestDatabase
import com.github.cstrerath.uncover.data.database.dao.RandQuestDatabaseDao

@Database(
    entities = [
        GameCharacter::class,
        Quest::class,
        QuestStep::class,
        CharacterQuestProgress::class,
        Location::class,
        RandQuestDatabase::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameCharacterDao(): GameCharacterDao
    abstract fun questDao(): QuestDao
    abstract fun questStepDao(): QuestStepDao
    abstract fun characterQuestProgressDao(): CharacterQuestProgressDao
    abstract fun locationDao(): LocationDao
    abstract fun randomQuestDatabaseDao(): RandQuestDatabaseDao

    companion object {
        private const val DATABASE_NAME = "app_database"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}
