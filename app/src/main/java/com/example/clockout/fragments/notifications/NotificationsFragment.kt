package com.example.clockout.fragments.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clockout.R
import com.example.clockout.SharedViewModel
import com.example.clockout.database.TimeCardDatabase
import com.example.clockout.databinding.FragmentNotificationsBinding

class NotificationsFragment() : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.i("NotificationsFragment", "Notifications onCreateView instantiated!")
        // Create binding to fragment layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false)

        // Create instance of the view model factory with inputs
        val application = requireNotNull(this.activity).application
        val dataSource = TimeCardDatabase.getInstance(application).timeCardDatabaseDao
        val viewModelFactory = NotificationsViewModelFactory(dataSource, application)

        // Get a reference to the viewmodel associated with this fragment
        notificationsViewModel = ViewModelProvider(
            this, viewModelFactory).get(NotificationsViewModel::class.java)


        // Create adapter for recyclerview
        val adapter = NotificationsAdapter(NotificationsListener { timeCardId ->
            Toast.makeText(context, "$timeCardId", Toast.LENGTH_LONG).show()
            notificationsViewModel.onEntryClicked(timeCardId)
        })
        // Attach adapter to binding
        binding.timecardHistoryLog.adapter = adapter  //attach Notification Adapter to recyclerview in notifications fragment

        //********** Observations ************

        // Observe changes to the list of entries, update list whenever it changes
        notificationsViewModel.allDays.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        notificationsViewModel.navigateToDetails.observe(viewLifecycleOwner, Observer { entry ->
            entry?.let{
                this.findNavController().navigate(
                    NotificationsFragmentDirections.actionNotificationsToNotificationsDetailFragment(entry))
                notificationsViewModel.onDetailsNavigated()
            }
        })

        // Attach viewModel to binding
        binding.notificationsViewModel = notificationsViewModel
        // Attach viewLifecycleOwner to binding
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.i("NotificationsFragment", "Notifications View resumed!")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("NotificationsFragment", "Notifications View destroyed!")
    }
}