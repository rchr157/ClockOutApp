package com.example.clockout.fragments.notifications

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.clockout.database.TimeCardEntry
import com.example.clockout.database.TimeCardDatabaseDao
//import com.example.clockout.formatAllData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar.*

class NotificationsViewModel(
    val database: TimeCardDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    // Entry for today
    private var today = MutableLiveData<TimeCardEntry?>()

    // All Entries
    val allDays = database.getAllDays()

    //
    private val _navigateToDetails = MutableLiveData<Long>()
    val navigateToDetails: LiveData<Long> = _navigateToDetails

    private val _addButtonClicked = MutableLiveData<Long>()
    val addButtonClicked: LiveData<Long> = _addButtonClicked

    // Today's Date
    private var _todayDate = MutableLiveData<String>()
    val todayDate : LiveData<String> = _todayDate

   @SuppressLint("SimpleDateFormat")
   private val dateFormat = SimpleDateFormat("EEE MMMM dd, yyyy")



    init {
        Log.i("NotificationsViewModel", "Notifications ViewModel instantiated!")
        _todayDate.value = dateFormat.format(getInstance().time).toString()
        initializeToday()
    }
    // Start checking
    private fun initializeToday() {
        viewModelScope.launch {
            today.value = getTodayFromDatabase()
        }
    }
     // Check if timecard entry is complete or still in progress
    private suspend fun getTodayFromDatabase(): TimeCardEntry? {
        var current = database.getToday()
        if (current?.clockOutTimeMilli != current?.clockInTimeMilli){
            current = null
        }
        return current
    }

    fun onEntryClicked(id: Long) {
        _navigateToDetails.value = id
    }

    fun onDetailsNavigated() {
        _navigateToDetails.value = null
    }

    fun onAddButtonClicked(){
        viewModelScope.launch {
            addNewEntry()
            openNewEntryDetail()
        }

    }

    private suspend fun addNewEntry() {
        val newDay = TimeCardEntry()  // Create new Time Card Entry
        insert(newDay)
    }
    private suspend fun openNewEntryDetail(){
        val entry = database.getToday()
        if (entry != null) {
            onEntryClicked(entry.timeCardId)
        }
    }


    //TODO: Add buttons to edit/modify previous entries

    // Database Calls (SQL commands)
    private suspend fun insert(current: TimeCardEntry){
        database.insert(current)
    }

    private suspend fun update(current: TimeCardEntry){
        database.update(current)
    }

    private suspend fun clear() {
        database.clearAll()
    }

    private suspend fun clearentry(current: TimeCardEntry){
        database.clearEntry(current)
    }




}