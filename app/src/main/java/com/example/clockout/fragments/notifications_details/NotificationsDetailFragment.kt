package com.example.clockout.fragments.notifications_details

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.clockout.R
import com.example.clockout.SharedViewModel
import com.example.clockout.database.TimeCardDatabase
import com.example.clockout.databinding.FragmentNotificationsDetailBinding
import com.example.clockout.fragments.dialog_fragments.DatePickerFragment
import com.example.clockout.fragments.dialog_fragments.TimePickerFragment
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NotificationsDetailFragment: Fragment(){
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Create Binding
        val binding: FragmentNotificationsDetailBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_notifications_detail, container, false)

        // Create View Model Factory and input parameters
        val application = requireNotNull(this.activity).application
        val arguments = NotificationsDetailFragmentArgs.fromBundle(arguments)
        val dataSource = TimeCardDatabase.getInstance(application).timeCardDatabaseDao
        val viewModelFactory = NotificationsDetailViewModelFactory(arguments.timeCardId, dataSource)

        // Create View Model
        val detailsViewModel = ViewModelProvider(
                this, viewModelFactory).get(NotificationsDetailViewModel::class.java)

        // Get a reference to the shared viewmodel
        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        binding.detailsViewModel = detailsViewModel
        binding.lifecycleOwner = viewLifecycleOwner



        // ********** Observations ************

        // Observe Navigation to Notification Fragment
        detailsViewModel.navigateToNotifications.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(
                        NotificationsDetailFragmentDirections.actionNotificationsDetailFragmentToNotifications())
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                detailsViewModel.doneNavigating()
            }
        })

        //      Observe Clicks          //
        // Observe Click on Date textview
        detailsViewModel.onDateClick.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val newFragment = DatePickerFragment()
                newFragment.show(requireActivity().supportFragmentManager, "datePicker")
                detailsViewModel.onDateClickComplete()
            }
        })
        // Observe Click on Start Time textview
        detailsViewModel.onStartTimeClick.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val newFragment = TimePickerFragment().newInstance(0)
                newFragment.show(requireActivity().supportFragmentManager, "startTimePicker")
                detailsViewModel.onStartTimeClickComplete()
            }
        })
        // Observe Click on Lunch Break Start textview
        detailsViewModel.onLunchStartClick.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val newFragment = TimePickerFragment().newInstance(1)
                newFragment.show(requireActivity().supportFragmentManager, "lunchStartTimePicker")
                detailsViewModel.onLunchStartClickComplete()
            }
        })
        // Observe Click on Lunch Break End textview
        detailsViewModel.onLunchEndClick.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val newFragment = TimePickerFragment().newInstance(2)
                newFragment.show(requireActivity().supportFragmentManager, "lunchEndTimePicker")
                detailsViewModel.onLunchEndClickComplete()
            }
        })
        // Observe Click on End Time textview
        detailsViewModel.onEndTimeClick.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val newFragment = TimePickerFragment().newInstance(3)
                newFragment.show(requireActivity().supportFragmentManager, "endTimePicker")
                detailsViewModel.onEndTimeClickComplete()
            }
        })

        //         Observe Date/Time Picker output values   //
        // Observe date selection from datepicker
        sharedViewModel.selectedDate.observe(requireActivity(), { dateString ->
            lifecycleScope.launch { detailsViewModel.updateEntry(date=dateString,
                    dow=sharedViewModel.selectedDayOfWeek.value) }
        })
        // Observe date selection from timepicker
        sharedViewModel.selectedStartTime.observe(requireActivity(), { startTime ->
            lifecycleScope.launch { detailsViewModel.updateEntry(start = startTime) }
        })
        // Observe date selection from timepicker
        sharedViewModel.selectedLunchStartTime.observe(requireActivity(), { lunchStartTime ->
            lifecycleScope.launch { detailsViewModel.updateEntry(lunchStart = lunchStartTime) }
        })
        // Observe date selection from timepicker
        sharedViewModel.selectedLunchEndTime.observe(requireActivity(), { lunchEndTime ->
            lifecycleScope.launch { detailsViewModel.updateEntry(lunchEnd = lunchEndTime) }
        })
        // Observe date selection from timepicker
        sharedViewModel.selectedEndTime.observe(requireActivity(), { endTime ->
            lifecycleScope.launch { detailsViewModel.updateEntry(end = endTime) }
        })


        // This callback will only be called when MyFragment is at least Started.
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                detailsViewModel.onCancel()
            }
        })



        return binding.root
    }



}