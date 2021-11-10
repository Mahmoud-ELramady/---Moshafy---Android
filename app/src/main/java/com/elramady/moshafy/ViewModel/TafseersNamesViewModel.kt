package com.elramady.moshafy.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elramady.moshafy.repo.QuranRepositary
import com.elramady.moshafy.vo.TafseersNames.TafseersNamesItem
import io.reactivex.rxjava3.disposables.CompositeDisposable

class TafseersNamesViewModel(var repo: QuranRepositary): ViewModel() {

    val compositeDisposable= CompositeDisposable()
  val  TafseersNamesList: LiveData<List<TafseersNamesItem>> by lazy{
        repo.fetchTafseersNames(compositeDisposable)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()

    }


}