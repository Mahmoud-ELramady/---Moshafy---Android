package com.elramady.moshafy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elramady.moshafy.ViewModel.TafseerTextViewModel
import com.elramady.moshafy.repo.QuranRepositary
import java.lang.IllegalArgumentException







class TafseerTextRequest
(val repositary: QuranRepositary,val tafseerId:Int,val surahNumber:Int,val ayahNumber:Int): ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TafseerTextViewModel::class.java)){
            return TafseerTextViewModel(repositary,tafseerId,surahNumber,ayahNumber) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")    }

}