package com.example.clockout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val selectedDate = MutableLiveData<String>()
    val selectedDayOfWeek = MutableLiveData<Int>()
    val selectedStartTime = MutableLiveData<Long>()
    val selectedLunchStartTime = MutableLiveData<Long>()
    val selectedLunchEndTime = MutableLiveData<Long>()
    val selectedEndTime = MutableLiveData<Long>()
}