package com.example.clockout.fragments.dialog_fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import androidx.preference.DialogPreference
import com.example.clockout.R
import java.text.SimpleDateFormat
import java.util.*

class DatePickerPreference(context: Context?, attrs: AttributeSet?): DialogPreference(
    context,
    attrs
){

    companion object{
        //By default we want notificaitons to appear at 9 AM each time
        private val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat")
        val DEFAULT_DATE: String = SimpleDateFormat("MMMM dd, yyyy").format(calendar.time)
    }


    override fun onSetInitialValue(defaultValue: Any?) {
        super.onSetInitialValue(defaultValue)
        summary = getPersistedDate()
    }

    /**
     * Gets the date as a string from the current data storage.
     *
     * @return string representation of the date.
     */
    fun getPersistedDate(): String{
        return super.getPersistedString(DEFAULT_DATE)
    }

    /**
     * Saves the date as a string in the current data storage.
     *
     * @param text string representation of the date to save.
     */
    fun setPersistedDate(text: String) {
        val wasBlocking: Boolean = shouldDisableDependents()
        super.persistString(text)
        val isBlocking: Boolean = shouldDisableDependents()

        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking)
        }
        notifyChanged()
    }
}