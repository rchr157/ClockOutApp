/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.clockout

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*

/**
 * These functions create a formatted string that can be set in a TextView.
 */

/**
 * Take the Long milliseconds returned by the system and stored in Room,
 * and convert it to a nicely formatted string for display.
 *
 * EEEE - Display the long letter version of the weekday
 * MMM - Display the letter abbreviation of the nmotny
 * dd-yyyy - day in month and full year numerically
 * HH:mm - Hours and minutes in 24hr format
 */
@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("MMMM dd, yyyy")
            .format(systemTime).toString()
}

@SuppressLint("SimpleDateFormat")
fun convertLongToTimeString(systemTime: Long): String {
    return SimpleDateFormat("hh:mm a")
        .format(systemTime).toString()
}
@SuppressLint("SimpleDateFormat")
fun convertLongToSingleDay(systemTime: Long): Int {
    return SimpleDateFormat("u").format(systemTime).toInt()
}

fun getCurrentDate(): String {
    return convertLongToDateString(System.currentTimeMillis())
}

fun getCurrentDayOfWeek(): Int {
    return convertLongToSingleDay(System.currentTimeMillis())
}

fun getCurrentTime(): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, 2000)  // Arbitrary Constant
    cal.set(Calendar.MONTH, 1)  // Arbitrary Constant
    cal.set(Calendar.DAY_OF_MONTH, 1)  // Arbitrary Constant
    cal.set(Calendar.SECOND, 0) // Round off seconds
    return cal.timeInMillis
}

fun minutesFromMidnightToHourlyTime(minutes: Int): String{
    var hours = minutes/60
    val min = minutes % 60
    val amPM = if (hours < 12) {
        "AM"
    } else {
        "PM"
    }

    if(hours>12){
        hours -= 12
    }

    return String.format("%02d:%02d %s", hours, min, amPM)
}

/***
 * These functions will handle notification reminders for clocking in and out of work
 */
// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.reminder_notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(applicationContext.getString(R.string.reminder_notification_channel_id))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}

