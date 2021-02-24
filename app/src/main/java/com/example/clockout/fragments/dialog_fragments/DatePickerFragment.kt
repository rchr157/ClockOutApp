package com.example.clockout.fragments.dialog_fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.clockout.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment : DialogFragment(), OnDateSetListener  {

    private var dateSetListener: OnDateSetListener? = null
    lateinit var datePicker: DatePickerDialog


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.i("DatePicker", "DatePicker onCreateDialog initiated")
        // Use current date as default date in picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Activity needs to implement this interface
//        dateSetListener = targetFragment as? OnDateSetListener

//        datePicker = DatePickerDialog(requireActivity(), dateSetListener, year, month, day)

        return DatePickerDialog(requireActivity(), this, year, month, day)
    }


    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        // Do Something with the date chosen
//        targetFragment?.processDatePickerResults()
        Log.i("DatePickerFragment", "onDateSet Initiated")
        val cal = Calendar.getInstance()
        cal.set(year, month, day)
        val dateString = SimpleDateFormat("MMMM dd, yyyy").format(cal.time).toString()
        val dayOfWeek = SimpleDateFormat("u").format(cal.time).toInt()

        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.selectedDayOfWeek.value = dayOfWeek
        sharedViewModel.selectedDate.value = dateString
    }


}