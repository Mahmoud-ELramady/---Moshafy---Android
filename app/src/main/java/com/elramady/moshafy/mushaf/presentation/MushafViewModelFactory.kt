package com.elramady.moshafy.mushaf.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MushafViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MushafReaderViewModel::class.java)) {
            return MushafReaderViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
