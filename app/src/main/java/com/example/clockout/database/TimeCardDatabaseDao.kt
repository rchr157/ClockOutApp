package com.example.clockout.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface TimeCardDatabaseDao {
    @Insert
    suspend fun insert(timeCardEntry: TimeCardEntry)

    @Update
    suspend fun update(timeCardEntry: TimeCardEntry)

    @Delete
    suspend fun clearEntry(timeCardEntry: TimeCardEntry)

    @Query("SELECT * from daily_timecard_table WHERE timeCardId = :key")
    suspend fun get(key: Long): TimeCardEntry?

    @Query("DELETE FROM daily_timecard_table")
    suspend fun clearAll()

    @Query("SELECT * FROM daily_timecard_table ORDER BY timeCardId DESC LIMIT 1")
    suspend fun getToday(): TimeCardEntry?

//    @Query("SELECT * FROM daily_timecard_table ORDER BY timeCardId DESC LIMIT 1")
//    fun getNow(): LiveData<List<TimeCardEntry>>
//    @Query("SELECT * FROM daily_timecard_table ORDER BY timeCardId DESC LIMIT 1")
//    fun getCurrent(): LiveData<TimeCardEntry>

    @Query("SELECT * FROM daily_timecard_table WHERE timeCardId = :key")
    fun getEntryWithId(key: Long): LiveData<TimeCardEntry>
    
    @Query("SELECT * FROM daily_timecard_table ORDER BY timeCardId DESC")
    fun getAllDays(): LiveData<List<TimeCardEntry>>
}