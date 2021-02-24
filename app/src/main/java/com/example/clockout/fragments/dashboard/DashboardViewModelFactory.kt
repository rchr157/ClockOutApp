package com.example.clockout.fragments.dashboard

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.clockout.database.TimeCardDatabaseDao
import java.lang.IllegalArgumentException

class DashboardViewModelFactory(
    private val dataSource: TimeCardDatabaseDao,
    private val application: Application,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return DashboardViewModel(dataSource, application, handle) as T
    }

//    @Suppress("unchecked_cast")
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
//            return DashboardViewModel(dataSource, application) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
}