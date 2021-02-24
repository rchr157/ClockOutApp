package com.example.clockout.receiver

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import com.example.clockout.R
import com.example.clockout.sendNotification


class AlarmReceiver: BroadcastReceiver() {

    private var alarmManager: AlarmManager? = null
    private var appContext: Context? = null
    private val REQUEST_CODE = 1
    private val TIME_NOT_SET: Long = 0

    fun MyAlarms(appContext: Context?) {
        this.appContext = appContext
        alarmManager = appContext!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED"){
            // TODO:
//            val prefs: SharedPreferences = appContext!!.getSharedPreferences("")
        }
        val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
                context.getText(R.string.reminder_clock_in_message).toString(),
                context)
    }
}