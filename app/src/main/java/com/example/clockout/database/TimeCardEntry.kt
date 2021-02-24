package com.example.clockout.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.clockout.getCurrentDate
import com.example.clockout.getCurrentDayOfWeek
import com.example.clockout.getCurrentTime
import java.util.*

@Entity(tableName = "daily_timecard_table")
data class TimeCardEntry(
        @PrimaryKey(autoGenerate = true)  // ID for TimeCard Entry
        val timeCardId: Long = 0L,

        @ColumnInfo(name = "date")  // Date of TimeCard Entry MM-DD-YYYY
        var timeCardDate: String = getCurrentDate(),

        @ColumnInfo(name = "day_of_week")  // Day of Week for TimeCard Entry
        var dayofweek: Int = getCurrentDayOfWeek(),

        @ColumnInfo(name = "clockin_time_milli")  // Clock in for TimeCard Entry
        var clockInTimeMilli: Long = getCurrentTime(),

        @ColumnInfo(name = "lunchin_time_milli")  // Clock in for Lunch break for TimeCard Entry
        var lunchInTimeMilli: Long = 0L,

        @ColumnInfo(name = "lunchout_time_milli")  // Clock out for Lunch break for TimeCard Entry
        var lunchOutTimeMilli: Long = 0L,

        @ColumnInfo(name = "clockout_time_milli")  // Clock out for TimeCard Entry
        var clockOutTimeMilli: Long = 0L,

        @ColumnInfo(name = "hours")
        var totalHours: Double = 0.0
)