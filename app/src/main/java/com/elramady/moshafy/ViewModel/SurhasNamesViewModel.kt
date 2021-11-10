package com.elramady.moshafy.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elramady.moshafy.repo.QuranRepositary
import com.elramady.moshafy.vo.SurahsNames.Data
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SurhasNamesViewModel(var repo: QuranRepositary): ViewModel() {
    val compositeDisposable= CompositeDisposable()

    val SurhasNamesList: LiveData<List<Data>> by lazy{
        repo.fetchSurhasNames(compositeDisposable)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()

    }


}