package com.example.clockout.fragments.dialog_fragments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TimePicker
import androidx.preference.DialogPreference
import androidx.preference.PreferenceDialogFragmentCompat
import com.example.clockout.minutesFromMidnightToHourlyTime

class TimePickerPreference(context: Context?, attrs: AttributeSet?): DialogPreference(context, attrs){

    companion object{
        //By default we want notificaitons to appear at 9 AM each time
        private const val DEFAULT_HOUR = 8
        const val DEFAULT_MINUTES_FROM_MIDNIGHT = DEFAULT_HOUR * 60
    }

    // Get saved preference value (in minutes from midnight, si 1AM is represented as 1*60
    fun getPersistedMinutesFromMidnight(): Int {
        return super.getPersistedInt(DEFAULT_MINUTES_FROM_MIDNIGHT)
    }
    //Save Preference
    fun persistMinutesFromMidnight(minutesFromMidnight: Int) {
        super.persistInt(minutesFromMidnight)
        notifyChanged()
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        super.onSetInitialValue(defaultValue)
        summary = minutesFromMidnightToHourlyTime(getPersistedMinutesFromMidnight())
    }

}