package com.example.clockout.fragments.notifications_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clockout.database.TimeCardDatabaseDao
import com.example.clockout.database.TimeCardEntry
import kotlinx.coroutines.launch
import kotlin.math.round

class NotificationsDetailViewModel(
    private val timeCardId: Long = 0L,
    dataSource: TimeCardDatabaseDao) : ViewModel() {

    val database = dataSource

    private var entry: LiveData<TimeCardEntry>
    private var timeCardDate: String? = null
    private var dayOfWeek: Int? = null
    private var clockInTime: Long? = null
    private var lunchInTime: Long? = null
    private var lunchOutTime: Long? = null
    private var clockOutTime: Long? = null



//    var entryId: MutableLiveData<Long>

    // Click Listeners:
    // Date Click Listener
    private val _onDateClick = MutableLiveData<Boolean>()
    val onDateClick: LiveData<Boolean> = _onDateClick
    // Start Time Click Listener
    private val _onStartTimeClick = MutableLiveData<Boolean>()
    val onStartTimeClick: LiveData<Boolean> = _onStartTimeClick
    // Lunch Start Click Listener
    private val _onLunchStartClick = MutableLiveData<Boolean>()
    val onLunchStartClick: LiveData<Boolean> = _onLunchStartClick
    // Lunch End Click Listener
    private val _onLunchEndClick = MutableLiveData<Boolean>()
    val onLunchEndClick: LiveData<Boolean> = _onLunchEndClick
    // End Time Click Listener
    private val _onEndTimeClick = MutableLiveData<Boolean>()
    val onEndTimeClick: LiveData<Boolean> = _onEndTimeClick


    private val _navigateToNotifications = MutableLiveData<Boolean?>()
    val navigateToNotifications: LiveData<Boolean?>
        get() = _navigateToNotifications



    fun getEntry() = entry

    init {
        entry = database.getEntryWithId(timeCardId)
    }

    // Handle Clicks
    fun onDateClick(){
        _onDateClick.value = true
    }
    fun onDateClickComplete(){
        _onDateClick.value = false
    }

    fun onStartTimeClick(){
        _onStartTimeClick.value = true
    }
    fun onStartTimeClickComplete(){
        _onStartTimeClick.value = false
    }

    fun onLunchStartClick(){
        _onLunchStartClick.value = true
    }
    fun onLunchStartClickComplete(){
        _onLunchStartClick.value = false
    }

    fun onLunchEndClick(){
        _onLunchEndClick.value = true
    }
    fun onLunchEndClickComplete(){
        _onLunchEndClick.value = false
    }

    fun onEndTimeClick(){
        _onEndTimeClick.value = true
    }
    fun onEndTimeClickComplete(){
        _onEndTimeClick.value = false
    }

    private fun navigateToNotifications() {
        _navigateToNotifications.value = true
    }

    fun doneNavigating() {
        _navigateToNotifications.value = null
    }

    fun onSave(){
        timeCheck()
        navigateToNotifications()
    }

    fun onCancel() {
        cancelUpdate()
        navigateToNotifications()
    }

    fun onDeleteClick() {
        deleteEntry()
        navigateToNotifications()
    }

    // Handle Functions

    private fun deleteEntry() {
        viewModelScope.launch {
            entry.value?.let { clearEntry(current = it) }
        }
    }

    private fun cancelUpdate() {
        Log.i("DetailViewModel", "Cancel Update initiated")
        viewModelScope.launch {
            updateEntry(date=timeCardDate, dow=dayOfWeek, start=clockInTime, lunchStart=lunchInTime,
            lunchEnd=lunchOutTime, end=clockOutTime)
            Log.i("DetailViewModel", "Cancel Update complete")
        }
    }

   suspend fun updateEntry(date: String?=null, dow:Int?=null, start:Long?=null,
                            lunchStart:Long?=null, lunchEnd:Long?=null, end:Long?=null){
        Log.i("DetailViewModel", "Update Entry initiated")

        val current: TimeCardEntry? = entry.value

        if (date != null && dow != null) {
            timeCardDate = entry.value?.timeCardDate
            dayOfWeek = entry.value?.dayofweek
            current?.timeCardDate = date
            current?.dayofweek = dow
        }
        if (start != null) {
            clockInTime = entry.value?.clockInTimeMilli
            current?.clockInTimeMilli = start
        }
        if (lunchStart != null) {
            lunchInTime = entry.value?.lunchInTimeMilli
            current?.lunchInTimeMilli = lunchStart
        }
        if (lunchEnd != null) {
            lunchOutTime = entry.value?.lunchOutTimeMilli
            current?.lunchOutTimeMilli = lunchEnd
        }
        if (end != null) {
            clockOutTime = entry.value?.clockOutTimeMilli
            current?.clockOutTimeMilli = end
        }

        if (current != null) {
            update(current)
        entry=database.getEntryWithId(timeCardId)
        }

        Log.i("DetailViewModel", "Update Entry complete")
    }

    private fun timeCheck(){
        Log.i("DetailViewModel", "Time Check initiated")
        viewModelScope.launch {
            val current: TimeCardEntry = entry.value ?: return@launch
        // If End time is less than Start Time
            if (current.clockOutTimeMilli < current.clockInTimeMilli) {
                Log.i("DetailViewModel", "TimeMismatch: Start Time is greater than End Time")
                return@launch
            }
        // If Lunch Start is less than Start Time
            if ((current.lunchInTimeMilli < current.clockInTimeMilli) &&
                    current.lunchInTimeMilli > 0)  {
                Log.i("DetailViewModel", "TimeMismatch: Start Time is greater than Lunch Start Time")
                return@launch
            }
        // If End time is less than Lunch Start
            if ((current.clockOutTimeMilli < current.lunchOutTimeMilli) &&
                    current.lunchOutTimeMilli > 0) {
                Log.i("DetailViewModel", "TimeMismatch: Lunch End Time is greater than End Time")
                return@launch
            }

            // Update Total Hours
            val workStart = current.clockInTimeMilli
            val workEnd = current.clockOutTimeMilli
            val lunchStart = current.lunchInTimeMilli
            val lunchEnd = current.lunchOutTimeMilli


            current.totalHours = calculateHours(workStart, workEnd, lunchStart, lunchEnd)

            update(current)
            entry=database.getEntryWithId(timeCardId)
        }
        Log.i("DetailViewModel", "Time Check complete")
    }

    private fun calculateHours(
            workStart: Long,
            workEnd: Long,
            lunchStart: Long,
            lunchEnd: Long): Double{
        // Check lunch Schedule
        val lunch = if (lunchStart > 0 && lunchEnd > 0) {
            lunchEnd - lunchStart
        } else {
            0
        }

        val work = if(workEnd > 0){
            (workEnd - workStart) - lunch
        } else {
            0
        }

        val hour = work / (60 * 60 * 1000)
        val min = work/ (60 * 1000)
        val mins = (min.toDouble()/60) - hour

        return hour + mins
    }


    // Database Calls (SQL commands)
    private suspend fun insert(current: TimeCardEntry){
        database.insert(current)
    }

    private suspend fun update(current: TimeCardEntry){
        database.update(current)
    }

    private suspend fun clearEntry(current: TimeCardEntry){
        database.clearEntry(current)
    }
}