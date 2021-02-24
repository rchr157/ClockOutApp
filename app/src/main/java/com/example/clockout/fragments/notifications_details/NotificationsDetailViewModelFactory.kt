package com.example.clockout.fragments.notifications_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clockout.database.TimeCardDatabaseDao
import java.lang.IllegalArgumentException

class NotificationsDetailViewModelFactory(
    private val timeCardId: Long,
    private val dataSource: TimeCardDatabaseDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsDetailViewModel::class.java)) {
            return NotificationsDetailViewModel(timeCardId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}