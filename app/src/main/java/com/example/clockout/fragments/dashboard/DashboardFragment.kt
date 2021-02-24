package com.example.clockout.fragments.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.example.clockout.R
import com.example.clockout.SharedViewModel
import com.example.clockout.database.TimeCardDatabase
import com.example.clockout.databinding.FragmentDashboardBinding
import com.example.clockout.fragments.dialog_fragments.DatePickerFragment
import com.example.clockout.fragments.dialog_fragments.TimePickerFragment
import kotlinx.coroutines.launch

class DashboardFragment() : Fragment() {

//    val vm: SavedStateHandle by viewModels()
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.i("DashbaordFragment", "Dashboard onCreateView instantiated!")
        // Create binding to fragment layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        // Create instance of the view model factory
        val application = requireNotNull(this.activity).application
        val dataSource = TimeCardDatabase.getInstance(application).timeCardDatabaseDao
        val viewModelFactory = DashboardViewModelFactory(dataSource, application, this)
        // Get a reference to the viewmodel associated with this fragment
        dashboardViewModel = ViewModelProvider(
            this, viewModelFactory).get(DashboardViewModel::class.java)

        // Get a reference to the shared viewmodel
        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Observe Changes in current entry
        dashboardViewModel.current.observe(viewLifecycleOwner, Observer {
            dashboardViewModel.refresh()
        })

        //      Observe Clicks          //
        // Observe Click on Start Time textview
        dashboardViewModel.onStartTimeClick.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val newFragment = TimePickerFragment().newInstance(0)
                newFragment.show(requireActivity().supportFragmentManager, "startTimePicker")
                dashboardViewModel.onStartTimeClickComplete()
            }
        })
        // Observe Click on Lunch Break Start textview
        dashboardViewModel.onLunchStartClick.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val newFragment = TimePickerFragment().newInstance(1)
                newFragment.show(requireActivity().supportFragmentManager, "lunchStartTimePicker")
                dashboardViewModel.onLunchStartClickComplete()
            }
        })
        // Observe Click on Lunch Break End textview
        dashboardViewModel.onLunchEndClick.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val newFragment = TimePickerFragment().newInstance(2)
                newFragment.show(requireActivity().supportFragmentManager, "lunchEndTimePicker")
                dashboardViewModel.onLunchEndClickComplete()
            }
        })
        // Observe Click on End Time textview
        dashboardViewModel.onEndTimeClick.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val newFragment = TimePickerFragment().newInstance(3)
                newFragment.show(requireActivity().supportFragmentManager, "endTimePicker")
                dashboardViewModel.onEndTimeClickComplete()
            }
        })

        //         Observe Date/Time Picker output values   //
        // Observe date selection from datepicker
        sharedViewModel.selectedDate.observe(requireActivity(), Observer { dateString ->
            lifecycleScope.launch { dashboardViewModel.updateEntry(date=dateString,
                    dow=sharedViewModel.selectedDayOfWeek.value) }
        })
        // Observe date selection from timepicker
        sharedViewModel.selectedStartTime.observe(requireActivity(), Observer { startTime ->
            lifecycleScope.launch { dashboardViewModel.updateEntry(start = startTime) }
        })
        // Observe date selection from timepicker
        sharedViewModel.selectedLunchStartTime.observe(requireActivity(), Observer { lunchStartTime ->
            lifecycleScope.launch { dashboardViewModel.updateEntry(lunchStart = lunchStartTime) }
        })
        // Observe date selection from timepicker
        sharedViewModel.selectedLunchEndTime.observe(requireActivity(), Observer { lunchEndTime ->
            lifecycleScope.launch { dashboardViewModel.updateEntry(lunchEnd = lunchEndTime) }
        })
        // Observe date selection from timepicker
        sharedViewModel.selectedEndTime.observe(requireActivity(), Observer { endTime ->
            lifecycleScope.launch { dashboardViewModel.updateEntry(end = endTime) }
        })


        binding.dashboardViewModel = dashboardViewModel
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }

}