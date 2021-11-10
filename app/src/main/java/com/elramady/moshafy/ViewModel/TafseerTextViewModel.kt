package com.elramady.moshafy.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elramady.moshafy.repo.QuranRepositary
import com.elramady.moshafy.vo.TafseerText.TafseerText
import io.reactivex.rxjava3.disposables.CompositeDisposable

class TafseerTextViewModel(repo: QuranRepositary,tafseerId:Int,surahNumber:Int,ayahNumber:Int): ViewModel(),androidx.databinding.Observable {
    private val compositeDisposable= CompositeDisposable()

    val TafseerTextList: LiveData<TafseerText> by lazy {
        repo.fetchTafseerText(compositeDisposable,tafseerId,surahNumber,ayahNumber)
    }
//    val removeLiveData by lazy {
//        repo.removeLiveData(compositeDisposable)
//    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    override fun addOnPropertyChangedCallback(callback: androidx.databinding.Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: androidx.databinding.Observable.OnPropertyChangedCallback?) {
    }
}