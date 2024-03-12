package com.elramady.moshafy.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException


@Suppress("UNCHECKED_CAST")
class JsonViewModelFactory(
    private val app: Application,
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JsonDataViewModel::class.java)){
            return JsonDataViewModel(app) as T

        }
        throw  IllegalArgumentException("view model not found")
    }
}