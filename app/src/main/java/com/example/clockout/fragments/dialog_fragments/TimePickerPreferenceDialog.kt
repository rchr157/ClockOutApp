package com.example.clockout.fragments.dialog_fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceDialogFragmentCompat
import com.example.clockout.minutesFromMidnightToHourlyTime

class TimePickerPreferenceDialog : PreferenceDialogFragmentCompat() {

    lateinit var timepicker: TimePicker

    companion object {
        fun newInstance(key: String): TimePickerPreferenceDialog  {
            val fragment = TimePickerPreferenceDialog()
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateDialogView(context: Context?): View {
        timepicker = TimePicker(context)
        return timepicker
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)

        val minutesAfterMidnight = (preference as TimePickerPreference).getPersistedMinutesFromMidnight()
        timepicker.setIs24HourView(false)
        timepicker.hour = minutesAfterMidnight / 60
        timepicker.minute = minutesAfterMidnight % 60
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDialogClosed(positiveResult: Boolean) {
        // Save Settings
        if (positiveResult) {
            val minutesAfterMidnight = (timepicker.hour * 60) + timepicker.minute
            (preference as TimePickerPreference).persistMinutesFromMidnight(minutesAfterMidnight)
            preference.summary = minutesFromMidnightToHourlyTime(minutesAfterMidnight)
        }
    }

}