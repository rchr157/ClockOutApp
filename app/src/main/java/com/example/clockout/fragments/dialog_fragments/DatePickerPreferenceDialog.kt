package com.example.clockout.fragments.dialog_fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.preference.PreferenceDialogFragmentCompat
import java.text.SimpleDateFormat
import java.util.*

class DatePickerPreferenceDialog : PreferenceDialogFragmentCompat() {

    private var mLastYear = 2021
    private var mLastMonth = 0
    private var mLastDay = 0
    private var mDatePicker: DatePicker? = null
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("MMMM dd, yyyy")

    companion object {
        fun newInstance(key: String?): DatePickerPreferenceDialog {
            val fragment = DatePickerPreferenceDialog()
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dateValue: String = getDatePreference().getPersistedDate()
        val calendar: Calendar = Calendar.getInstance()
        if (dateValue.isNotEmpty()) {
            calendar.time = dateFormat.parse(dateValue)
        }

        mLastYear = calendar.get(Calendar.YEAR)
        mLastMonth = calendar.get(Calendar.MONTH) + 1
        mLastDay = calendar.get(Calendar.DAY_OF_MONTH)
    }

    override fun onCreateDialogView(context: Context?): View? {
        mDatePicker = DatePicker(getContext())
        return mDatePicker
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)
        mDatePicker!!.updateDate(mLastYear, mLastMonth - 1, mLastDay)
    }


    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val cal = Calendar.getInstance()
            mLastYear = mDatePicker!!.year
            mLastMonth = mDatePicker!!.month
            mLastDay = mDatePicker!!.dayOfMonth

            cal.set(mLastYear, mLastMonth, mLastDay)
            val dateVal = dateFormat.format(cal.time)
            val preference: DatePickerPreference = getDatePreference()
            if (preference.callChangeListener(dateVal)) {
                preference.setPersistedDate(dateVal)
            }
            preference.summary = dateVal
        }
    }

    private fun getDatePreference(): DatePickerPreference {
        return preference as DatePickerPreference
    }

}