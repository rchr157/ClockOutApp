package com.example.clockout.fragments.notifications

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clockout.database.TimeCardDatabaseDao
import java.lang.IllegalArgumentException

class NotificationsViewModelFactory(
    private val dataSource: TimeCardDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            return NotificationsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}