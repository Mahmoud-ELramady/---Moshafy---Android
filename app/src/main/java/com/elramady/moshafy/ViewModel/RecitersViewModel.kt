package com.elramady.moshafy.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elramady.moshafy.repo.QuranRepositary
import com.elramady.moshafy.vo.RecitersNames.Reciter
import io.reactivex.rxjava3.disposables.CompositeDisposable

class RecitersViewModel(repositary: QuranRepositary):ViewModel() {
    private val compositeDisposable= CompositeDisposable()

    val recitersNamesList:LiveData<List<Reciter>> by lazy {
        repositary.fetchReciterName(compositeDisposable)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}