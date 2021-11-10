package com.elramady.moshafy.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elramady.moshafy.repo.QuranRepositary
import com.elramady.moshafy.vo.SurahText.SurahText
import io.reactivex.rxjava3.disposables.CompositeDisposable

class TextArabicViewModel(repo:QuranRepositary, id: Int): ViewModel() {
    private val compositeDisposable= CompositeDisposable()

val SurahsTextList:LiveData<SurahText> by lazy {
    repo.fetchSurhasText(compositeDisposable,id)
}


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}