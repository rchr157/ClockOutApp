package com.example.clockout.fragments.settings

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.preference.*
import com.example.clockout.R
import com.example.clockout.fragments.dialog_fragments.DatePickerPreference
import com.example.clockout.fragments.dialog_fragments.DatePickerPreferenceDialog
import com.example.clockout.fragments.dialog_fragments.TimePickerPreference
import com.example.clockout.fragments.dialog_fragments.TimePickerPreferenceDialog
import com.example.clockout.sendNotification
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {


    private val TIME_DIALOG_FRAGMENT_TAG = "TimePickerDialog"
    private val DATE_DIALOG_FRAGMENT_TAG = "DatePickerDialog"

    private val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener() { sharedPreference: SharedPreferences, key: String ->
            if (key == "reminder_clock_in") {
                Log.i("SettingsFragment", "Preference value was updated to: " + sharedPreference.getInt(key, -1).toString())
//                val alarmManager = context.get
                // Working
                val notificationManager = ContextCompat.getSystemService(requireContext(),
                    NotificationManager::class.java) as NotificationManager
                notificationManager.sendNotification(requireContext().getString(R.string.reminder_clock_in_message), requireContext())
            } else if (key == "reminder_clock_out") {
                Log.i("SettingsFragment", "Preference value was updated to: " + sharedPreference.getString(key, ""))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createChannel(getString(R.string.reminder_notification_channel_id),
            getString(R.string.reminder_header))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        // Use numerical only keyboard for hour goals edit text preference
        val editTextPreference:EditTextPreference? = findPreference("hour_goals")
        editTextPreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        when (preference) {
            is TimePickerPreference -> {
                val timePickerDialog = TimePickerPreferenceDialog.newInstance(preference.key)
                timePickerDialog.setTargetFragment(this,0)
                timePickerDialog.show(parentFragmentManager, TIME_DIALOG_FRAGMENT_TAG)
            }
            is DatePickerPreference -> {
                val datePickerDialog = DatePickerPreferenceDialog.newInstance(preference.key)
                datePickerDialog.setTargetFragment(this,0)
                datePickerDialog.show(parentFragmentManager, DATE_DIALOG_FRAGMENT_TAG)

            }
            else -> {
                super.onDisplayPreferenceDialog(preference)
            }
        }
    }


    private fun createChannel(channelId: String, channelName: String){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName,
            NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Reminder Description"

            val notificationManager = requireActivity()
                .getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

//    private fun startAlarm(){
//        val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
//    }

    private fun cancelAlarm(){}

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

}




