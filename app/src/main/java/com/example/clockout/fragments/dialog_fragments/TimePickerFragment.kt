package com.example.clockout.fragments.dialog_fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.clockout.SharedViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    fun newInstance(targetTime: Int): TimePickerFragment {
        val args = Bundle()
        args.putInt("target", targetTime)
        val fragment = TimePickerFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

//        var listener:TimePickerDialog.OnTimeSetListener = this as TimePickerDialog.OnTimeSetListener
//        listener.target
        // create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, is24HourFormat(activity))
    }

    @SuppressLint("SimpleDateFormat")
    override fun onTimeSet(timePicker: TimePicker?, hour: Int, minute: Int) {
        // Do Something with hte time chosen by the user

        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, 2000)  // Arbitrary Constant
        cal.set(Calendar.MONTH,1)  // Arbitrary Constant
        cal.set(Calendar.DAY_OF_MONTH, 1)  // Arbitrary Constant

        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        val timeInMilli = cal.timeInMillis

        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        when (requireArguments().getInt("target")) {
            0 -> {
                sharedViewModel.selectedStartTime.value = timeInMilli
            }
            1 -> {
                sharedViewModel.selectedLunchStartTime.value = timeInMilli
            }
            2 -> {
                sharedViewModel.selectedLunchEndTime.value = timeInMilli
            }
            3 -> {
                sharedViewModel.selectedEndTime.value = timeInMilli
            }

        }

    }
}