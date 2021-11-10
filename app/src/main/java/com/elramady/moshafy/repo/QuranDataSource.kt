package com.elramady.moshafy.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elramady.moshafy.Api.SwarInterface
import com.elramady.moshafy.ViewModel.SingleLiveEvent
import com.elramady.moshafy.vo.AzkarListen.AzkarListeningItem
import com.elramady.moshafy.vo.RecitersDetails.RecitersDetails
import com.elramady.moshafy.vo.RecitersNames.Reciter
import com.elramady.moshafy.vo.SurahText.SurahText
import com.elramady.moshafy.vo.SurahsNames.Data
import com.elramady.moshafy.vo.TafseerText.TafseerText
import com.elramady.moshafy.vo.TafseersNames.TafseersNamesItem
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception

class QuranDataSource(val apiService: SwarInterface, val compositeDisposable: CompositeDisposable) {



    private  var _downloadSurahsNamesDataResponse= MutableLiveData<List<Data>>()
    val downloadSwarDataResponse: LiveData<List<Data>>
        get() = _downloadSurahsNamesDataResponse



    private  var _downloadSurahsTextDataResponse= MutableLiveData<SurahText>()
    val downloadSurahsTextDataResponse: LiveData<SurahText>
        get() = _downloadSurahsTextDataResponse


    private var _downloadRecitersNames=MutableLiveData<List<Reciter>>()
    val downloadRecitersNames: LiveData<List<Reciter>>
        get()= _downloadRecitersNames


    private var _downloadRecitions=MutableLiveData<RecitersDetails>()
    val downloadRecitions: LiveData<RecitersDetails>
        get()= _downloadRecitions

    private var _downloadTasfseersNames=MutableLiveData<List<TafseersNamesItem>>()
    val downloadTasfseersNames: LiveData<List<TafseersNamesItem>>
        get()= _downloadTasfseersNames

    var _downloadTasfseerText=SingleLiveEvent<TafseerText>()
    val downloadTasfseerText: LiveData<TafseerText>
        get()= _downloadTasfseerText

    private var _downloadAzkarListening=MutableLiveData<List<AzkarListeningItem>>()
    val downloadAzkarListening: LiveData<List<AzkarListeningItem>>
        get()= _downloadAzkarListening


    // Get Name Surhas
    fun fetchSurahs(){

        try {
            compositeDisposable.add(apiService.getSurhasNames()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _downloadSurahsNamesDataResponse.postValue(it.data)
                    Log.e("SurhasNames", "Success")

                }, {
                    it.message?.let { it1 -> Log.e("Access", it1) }

                }
                )
            )
        }catch (e: Exception){
            e.message?.let { Log.e("Quran", it) }

        }
    }


//  Get Surahs Text
    fun fetchSurahsText(id:Int){
        try {
            compositeDisposable.add(apiService.getSurahsText(id)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _downloadSurahsTextDataResponse.postValue(it)
                        Log.e("SurhasText", "Success")

                    }, {
                        it.message?.let { it1 -> Log.e("SurhasTextfail", it1) }

                    }
                    )
            )
        }catch (e: Exception){
            e.message?.let { Log.e("Quran", it) }

        }
    }


    // Get Reciters Names
    fun fetchRecitersNames(){

        compositeDisposable.add(apiService.getRecitersNames()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {

                    _downloadRecitersNames.postValue(it.reciters)
                },{
                    Log.e("failReciters",it.message.toString())
                }
            )
        )


    }



    //get Recitions
    fun fetchRecitions(reciter_id :String){
        compositeDisposable.add(
                apiService.getRecitations(reciter_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(
                                {
                                    _downloadRecitions.postValue(it)
                                    Log.e("getReciationsPAss","getReciationsPAss")
                                },{
                            it.message?.let { it1 -> Log.e("FailReciations", it1) }
                        }
                        )


        )

    }



    //get Tafseers Names

    fun fetchTafseersNames(){

        try {
            compositeDisposable.add(apiService.getTafseersNames()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _downloadTasfseersNames.postValue(it)
                    Log.e("TasfseersNames", "TasfseersSuccess")

                }, {
                    it.message?.let { it1 -> Log.e("Access", it1) }

                }
                )
            )
        }catch (e: Exception){
            e.message?.let { Log.e("Tasfseers", it) }

        }
    }

    //  Get Surahs Text
    fun fetchTafseerText(tafseerId:Int,surahNumber:Int,ayahNumber:Int){
        try {
            compositeDisposable.add(
                    apiService.getTafseerTextAyah(tafseerId,surahNumber,ayahNumber)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _downloadTasfseerText.postValue(it)
                    Log.e("TafseerText", "Success")

                }, {
                    it.message?.let { it1 -> Log.e("TafseerTextfail", it1) }

                }
                )
            )
        }catch (e: Exception){
            e.message?.let { Log.e("QuranTafseer", it) }

        }
    }




    fun fetchAzkarListening(){

        try {
            compositeDisposable.add(apiService.getAzkarListening()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _downloadAzkarListening.postValue(it)
                    Log.e("TasfseersNames", "TasfseersSuccess")

                }, {
                    it.message?.let { it1 -> Log.e("Access", it1) }

                }
                )
            )
        }catch (e: Exception){
            e.message?.let { Log.e("Tasfseers", it) }

        }
    }



//    fun removeLiveData(){
//        _downloadTasfseerText.value=null
//    }






}