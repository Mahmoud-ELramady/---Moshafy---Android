package com.elramady.moshafy.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elramady.moshafy.repo.QuranRepositary
import com.elramady.moshafy.vo.RecitersDetails.RecitersDetails
import io.reactivex.rxjava3.disposables.CompositeDisposable

class RecitionsViewModel(repositary: QuranRepositary,idReciter:String):ViewModel() {
    val compositeDisposable=CompositeDisposable()

    val Recitions:LiveData<RecitersDetails> by lazy {
        repositary.fetchRecitions(compositeDisposable,idReciter)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}