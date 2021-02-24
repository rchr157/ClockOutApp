package com.example.clockout.fragments.dashboard

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.clockout.database.TimeCardEntry
import com.example.clockout.database.TimeCardDatabaseDao
import com.example.clockout.getCurrentDate
import com.example.clockout.getCurrentTime
//import com.example.clockout.formatAllData
//import com.example.clockout.formatData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar.*

class DashboardViewModel(
        val database: TimeCardDatabaseDao,
        application: Application,
        private val savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {


    // Entry for today
    private var today = MutableLiveData<TimeCardEntry?>()



    private var entry= MutableLiveData<TimeCardEntry>()
    val current:LiveData<TimeCardEntry> = entry
    fun getEntry() = entry


    // Click Listeners:
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

    // Button Press
    private val _clockButtonPressed: MutableLiveData<Boolean> = savedStateHandle.getLiveData(
        CLOCK_BUTTON_PRESSED_KEY)
    val clockButtonPressed : LiveData<Boolean> = _clockButtonPressed
    private val _lunchButtonPressed: MutableLiveData<Boolean> = savedStateHandle.getLiveData(
        LUNCH_BUTTON_PRESSED_KEY)
    val lunchButtonPress : LiveData<Boolean> = _lunchButtonPressed
    // Button Visible
    private val _clockButtonVisible: MutableLiveData<Boolean> = savedStateHandle.getLiveData(
        CLOCK_BUTTON_VISIBLE_KEY)
    val clockButtonVisible: LiveData<Boolean> = _clockButtonVisible

    private val _lunchButtonVisible: MutableLiveData<Boolean> = savedStateHandle.getLiveData(
        LUNCH_BUTTON_VISIBLE_KEY)
    val lunchButtonVisible: LiveData<Boolean> = _lunchButtonVisible
    // Button Text
    private val _clockButtonText: MutableLiveData<String> = savedStateHandle.getLiveData(
        CLOCK_BUTTON_TEXT_KEY)
    val clockButtonText: LiveData<String> = _clockButtonText

    private val _lunchButtonText: MutableLiveData<String> = savedStateHandle.getLiveData(LUNCH_BUTTON_TEXT_KEY)
    val lunchButtonText: LiveData<String> = _lunchButtonText


   @SuppressLint("SimpleDateFormat")
   private val dateFormat = SimpleDateFormat("EEE MMMM dd, yyyy")

    // Keep the key as a constant
    companion object {
        private val CLOCK_BUTTON_VISIBLE_KEY = "clockButtonVisible"
        private val CLOCK_BUTTON_PRESSED_KEY = "clockButtonPressed"
        private val CLOCK_BUTTON_TEXT_KEY = "clockButtonText"
        private val LUNCH_BUTTON_VISIBLE_KEY = "lunchButtonVisible"
        private val LUNCH_BUTTON_PRESSED_KEY = "lunchButtonPressed"
        private val LUNCH_BUTTON_TEXT_KEY = "lunchButtonText"

    }

    // Handle Button States
    fun saveCurrentState() {
        savedStateHandle.set(CLOCK_BUTTON_VISIBLE_KEY, _clockButtonVisible.value)
        savedStateHandle.set(CLOCK_BUTTON_PRESSED_KEY, _clockButtonPressed.value)
        savedStateHandle.set(LUNCH_BUTTON_VISIBLE_KEY, _lunchButtonVisible.value)
        savedStateHandle.set(LUNCH_BUTTON_PRESSED_KEY, _lunchButtonPressed.value)
        savedStateHandle.set(CLOCK_BUTTON_TEXT_KEY, _clockButtonText.value)
        savedStateHandle.set(LUNCH_BUTTON_TEXT_KEY, _lunchButtonText.value)
        Log.i("DashboardViewModel", "Button States Saved to handle")
    }
    private fun loadData() {
        _clockButtonPressed.value = savedStateHandle.getLiveData<Boolean?>(CLOCK_BUTTON_PRESSED_KEY).value?: false
        _clockButtonVisible.value = savedStateHandle.getLiveData<Boolean?>(CLOCK_BUTTON_VISIBLE_KEY).value?: true
        _clockButtonText.value = savedStateHandle.getLiveData<String?>(CLOCK_BUTTON_TEXT_KEY).value?: "Clock In"
        _lunchButtonPressed.value = savedStateHandle.getLiveData<Boolean?>(LUNCH_BUTTON_PRESSED_KEY).value?: false
        _lunchButtonVisible.value = savedStateHandle.getLiveData<Boolean?>(LUNCH_BUTTON_VISIBLE_KEY).value?: false
        _lunchButtonText.value = savedStateHandle.getLiveData<String?>(LUNCH_BUTTON_TEXT_KEY).value?: "Start Break"
        Log.i("DashboardViewModel", "Button States Loaded from handle")

    }

    init {
        Log.i("DashbaordViewModel", "Dashboard ViewModel instantiated!")
//        entry = database.getCurrent()
        initializeToday()
    }

    fun refresh() {
        initializeToday()
    }

    // Start checking
    private fun initializeToday() {
        viewModelScope.launch {
            entry.value = getTodayFromDatabase()

            if (entry.value == null) {
                return@launch
            } else if (entry.value?.lunchInTimeMilli!! != 0L &&
                entry.value?.lunchOutTimeMilli == 0L) {
                // if entry has a start lunch time but no end lunch time
                setButtonsLunchStart()
            } else if (entry.value?.lunchOutTimeMilli != 0L &&
                entry.value?.clockOutTimeMilli!! == 0L){
                // if entry has end lunch time but no clock out time
                setButtonsLunchEnd()
            } else if (entry.value?.lunchInTimeMilli!! == 0L &&
                    entry.value?.clockOutTimeMilli!! == 0L){
                // if entry does not have start lunch time or clock out time
                setButtonsWorkStart()
            }
        }
    }
     // Check if timecard entry is complete or still in progress
    private suspend fun getTodayFromDatabase(): TimeCardEntry? {
        var current = database.getToday()
        if (current?.clockOutTimeMilli != current?.clockInTimeMilli &&
                current?.timeCardDate != getCurrentDate()){
            // Reset
            current = null
            resetButtons()
        } else {
            loadData()
        }
        return current
    }

    private fun resetButtons(){
        _clockButtonPressed.value = false
        _clockButtonVisible.value = true
        _clockButtonText.value = "Clock In"
        _lunchButtonPressed.value = false
        _lunchButtonVisible.value = false
        _lunchButtonText.value = "Start Break"
    }

    private fun setButtonsWorkStart(){
        _clockButtonPressed.value = true
        _clockButtonVisible.value = true
        _clockButtonText.value = "Clock Out"
        _lunchButtonPressed.value = false
        _lunchButtonVisible.value = true
        _lunchButtonText.value = "Start Break"
    }

    private fun setButtonsLunchStart(){
        _clockButtonPressed.value = true
        _clockButtonVisible.value = false
        _clockButtonText.value = "Clock Out"
        _lunchButtonPressed.value = true
        _lunchButtonVisible.value = true
        _lunchButtonText.value = "End Break"
    }

    private fun setButtonsLunchEnd(){
        _clockButtonPressed.value = true
        _clockButtonVisible.value = true
        _clockButtonText.value = "Clock Out"
        _lunchButtonPressed.value = false
        _lunchButtonVisible.value = false
        _lunchButtonText.value = "Start Break"
    }

    private fun setButtonsWorkEnd(){
        resetButtons()
    }

    // Handle Clicks
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

    // Handle Button Presses
    fun onClockPress() {

        if (_clockButtonPressed.value == false){
            // If clock button has not been pressed, then start new entry
            onStartWorking()

        } else if (_clockButtonPressed.value == true && _lunchButtonPressed.value == false) {
            // if clock button has been pressed and lunch button has not been pressed or
            // has been reset, then close out entry
            onStopWork()
            onCalculateHours()

        } else {
            // Todo: Throw warning sign that both lunch fields must be filled or empty
        }
        // Save button states in savedStateHandle
        saveCurrentState()
    }

    fun onLunchPress(){
        if (_lunchButtonPressed.value == false){
            onStartLunch()
        } else {
            onStopLunch()
        }
        // Save button states in savedStateHandle
        saveCurrentState()
    }



    // Functions to edit entry fields
    private fun onStartWorking() {
        viewModelScope.launch {
            val newDay = TimeCardEntry()  // Create new Time Card Entry
            insert(newDay)  // Enter new Time Card Entry
            entry.value = getTodayFromDatabase()  // Save New Entry in today value
            setButtonsWorkStart()
//            _clockButtonPressed.value = true  // Update Button Press
//            _lunchButtonVisible.value = true  // Make Lunch Button Visible
//            _clockButtonText.value = "Clock Out"
//            _lunchButtonText.value = "Start Break"
        }
    }

    private fun onStartLunch() {
        viewModelScope.launch {
            val currentDay = entry.value ?: return@launch
            currentDay.lunchInTimeMilli = getCurrentTime()
            update(currentDay)  // Update Entry
//            _lunchButtonPressed.value = true
//            _clockButtonVisible.value = false  // Disable clock button
//            _lunchButtonText.value = "End Break"

            setButtonsLunchStart()
            entry.value = getTodayFromDatabase()
        }
    }

    private fun onStopLunch(){
        viewModelScope.launch {
            val currentDay = entry.value ?: return@launch
            currentDay.lunchOutTimeMilli = getCurrentTime()
            update(currentDay)
//            _lunchButtonText.value = "Start Break" // Reset Button Text
//            _lunchButtonVisible.value = false  // Disable Lunch Button
//            _lunchButtonPressed.value = false  // Reset Button Press
//
//            _clockButtonVisible.value = true // Enable clock button

            setButtonsLunchEnd()
            entry.value = getTodayFromDatabase()
        }
    }

    private fun onStopWork(){
        viewModelScope.launch {
            val currentDay = entry.value ?: return@launch
            currentDay.clockOutTimeMilli = getCurrentTime()
            update(currentDay)

//            _lunchButtonVisible.value = false  // Disable Lunch Button
//            _lunchButtonPressed.value = false  // Reset Button Press
//
//            _clockButtonPressed.value = false  // Reset Button Press
//            _clockButtonText.value = "Clock In" // Reset Button Text

            entry.value = getTodayFromDatabase()
            setButtonsWorkEnd()
        }
    }

    private fun onCalculateHours(){
        viewModelScope.launch{
            val current = entry.value ?: return@launch
            val workStart = current.clockInTimeMilli
            val workEnd = current.clockOutTimeMilli
            val lunchStart = current.lunchInTimeMilli
            val lunchEnd = current.lunchOutTimeMilli
            current.totalHours = calculateHours(workStart, workEnd, lunchStart, lunchEnd)

            update(current)
            entry.value = getTodayFromDatabase()
        }
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

    suspend fun updateEntry(date: String?=null, dow:Int?=null, start:Long?=null,
                            lunchStart:Long?=null, lunchEnd:Long?=null, end:Long?=null){
        Log.i("DetailViewModel", "Update Entry initiated")
        val current:TimeCardEntry? = entry.value

        if (date != null && dow != null) {
//            timeCardDate = entry.value?.timeCardDate
//            dayOfWeek = entry.value?.dayofweek
            current?.timeCardDate = date
            current?.dayofweek = dow
        }
        if (start != null) {
//            clockInTime = entry.value?.clockInTimeMilli
            current?.clockInTimeMilli = start
        }
        if (lunchStart != null) {
//            lunchInTime = entry.value?.lunchInTimeMilli
            current?.lunchInTimeMilli = lunchStart
        }
        if (lunchEnd != null) {
//            lunchOutTime = entry.value?.lunchOutTimeMilli
            current?.lunchOutTimeMilli = lunchEnd
        }
        if (end != null) {
//            clockOutTime = entry.value?.clockOutTimeMilli
            current?.clockOutTimeMilli = end
        }

        if (current != null) {
            onCalculateHours()
            update(current)
            entry.value=getTodayFromDatabase()
        }
        Log.i("DetailViewModel", "Update Entry complete")
    }


    // Database Calls (SQL commands)
    private suspend fun insert(current: TimeCardEntry){
        database.insert(current)
    }

    private suspend fun update(current: TimeCardEntry){
        database.update(current)
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("DashbaordFragment", "Dashboard ViewModel cleared!")
    }

}