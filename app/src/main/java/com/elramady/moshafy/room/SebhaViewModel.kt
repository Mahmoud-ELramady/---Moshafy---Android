package com.elramady.moshafy.room

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elramady.moshafy.repo.SebhaRepository
import com.elramady.moshafy.vo.sebha.SebhaData
import kotlinx.coroutines.launch

class SebhaViewModel(val repository: SebhaRepository):ViewModel() {

    private val _getAllSebhaLivaData= MutableLiveData<List<SebhaData>?>()
    val getAllSebhaLivaData: LiveData<List<SebhaData>?>
        get() = _getAllSebhaLivaData

    private val _getSumNumberOfAllSebhaLivaData= MutableLiveData<Int>()
    val getSumNumberOfAllSebhaLivaData: LiveData<Int>
        get() = _getSumNumberOfAllSebhaLivaData


    // wrong error because live data work only main thread
//    fun getAllSebha(){
//        viewModelScope.launch{
//            _getAllSebhaLivaData.postValue(repository.getAllSebha().value)
//        }
//    }

    fun getAllSebha()=repository.getAllSebha()

    fun getSumNumberOfAllSebha(){
        viewModelScope.launch{
            _getSumNumberOfAllSebhaLivaData.postValue(repository.getSumNumberOfAllSebha().value)
        }
    }

    fun insertSebha(sebha: SebhaData){
        viewModelScope.launch {
            repository.insertSebha(sebha)
        }
    }

    fun updateSebha(countSebha:Int,idSebha:Int){
        viewModelScope.launch {
            repository.updateSebha(countSebha,idSebha)
        }
    }

    fun deleteCountOfSebha(idSebha:Int){
        viewModelScope.launch {
            repository.deleteCountOfSebha(idSebha)
        }
    }

    fun deleteAllCountOfSebha(){
        viewModelScope.launch {
            repository.deleteAllCountOfSebha()
        }
    }

    fun deleteSebha(idSebha:Int){
        viewModelScope.launch {
            repository.deleteSebha(idSebha)
        }
    }
}