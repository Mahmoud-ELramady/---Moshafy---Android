package com.elramady.moshafy.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elramady.moshafy.repo.QuranRepositary
import com.elramady.moshafy.vo.AzkarListen.AzkarListeningItem
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AzkarListenViewModel (repositary: QuranRepositary): ViewModel() {
    val compositeDisposable= CompositeDisposable()

    val azkar: LiveData<List<AzkarListeningItem>> by lazy {
        repositary.fetchAzkarListening(compositeDisposable)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}