package com.github.cstrerath.uncover.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import android.util.Log
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.cstrerath.uncover.data.database.dao.AchievementDao
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
import com.github.cstrerath.uncover.data.database.entities.Achievement

@Database(
    entities = [
        GameCharacter::class,
        Quest::class,
        QuestStep::class,
        CharacterQuestProgress::class,
        Location::class,
        RandQuestDatabase::class,
        Achievement::class
    ],
    version = 6,
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
    abstract fun achievementDao(): AchievementDao

    companion object {
        private const val DATABASE_NAME = "app_database"
        private const val TAG = "AppDatabase"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Log.d(TAG, "Creating new database instance")
                instance ?: buildDatabase(context).also {
                    instance = it
                    Log.d(TAG, "Database instance created successfully")
                }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            Log.d(TAG, "Building database with version 6")
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.i(TAG, "Database created")
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Log.d(TAG, "Database opened")
                }
            }).build()
        }
    }

}
