package com.example.clockout.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TimeCardEntry::class], version = 1, exportSchema = false)
abstract class TimeCardDatabase : RoomDatabase() {

    abstract val timeCardDatabaseDao : TimeCardDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE : TimeCardDatabase? = null
        fun getInstance(context: Context): TimeCardDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TimeCardDatabase::class.java,
                        "timecard_history_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}