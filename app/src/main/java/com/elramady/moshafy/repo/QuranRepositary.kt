package com.elramady.moshafy.repo

import androidx.lifecycle.LiveData
import com.elramady.moshafy.Api.SwarInterface
import com.elramady.moshafy.vo.AzkarListen.AzkarListeningItem
import com.elramady.moshafy.vo.RecitersDetails.RecitersDetails
import com.elramady.moshafy.vo.RecitersNames.Reciter
import com.elramady.moshafy.vo.SurahText.SurahText
import com.elramady.moshafy.vo.SurahsNames.Data
import com.elramady.moshafy.vo.TafseerText.TafseerText
import com.elramady.moshafy.vo.TafseersNames.TafseersNamesItem
import io.reactivex.rxjava3.disposables.CompositeDisposable

class QuranRepositary(private val apiService: SwarInterface) {
    lateinit var quranDataSource:QuranDataSource

    fun fetchSurhasNames(compositeDisposable: CompositeDisposable): LiveData<List<Data>> {
        quranDataSource= QuranDataSource( apiService,compositeDisposable)
        quranDataSource.fetchSurahs()
        return quranDataSource.downloadSwarDataResponse
    }





    fun fetchSurhasText(compositeDisposable: CompositeDisposable,id:Int):LiveData<SurahText>{
        quranDataSource= QuranDataSource( apiService,compositeDisposable)
        quranDataSource.fetchSurahsText(id)
        return quranDataSource.downloadSurahsTextDataResponse
    }


    fun fetchReciterName(compositeDisposable: CompositeDisposable) : LiveData<List<Reciter>>{
        quranDataSource= QuranDataSource(apiService,compositeDisposable)
        quranDataSource.fetchRecitersNames()
        return quranDataSource.downloadRecitersNames

    }



    fun fetchRecitions(compositeDisposable: CompositeDisposable, idReciter:String) : LiveData<RecitersDetails>{
        quranDataSource= QuranDataSource(apiService,compositeDisposable)
        quranDataSource.fetchRecitions(idReciter)
        return quranDataSource.downloadRecitions
    }


    fun fetchTafseersNames(compositeDisposable: CompositeDisposable): LiveData<List<TafseersNamesItem>> {
        quranDataSource= QuranDataSource( apiService,compositeDisposable)
        quranDataSource.fetchTafseersNames()
        return quranDataSource.downloadTasfseersNames
    }



    fun fetchTafseerText(compositeDisposable: CompositeDisposable,tafseerId:Int,surahNumber:Int,ayahNumber:Int):LiveData<TafseerText>{
        quranDataSource= QuranDataSource( apiService,compositeDisposable)
        quranDataSource.fetchTafseerText(tafseerId,surahNumber,ayahNumber)
        return quranDataSource.downloadTasfseerText
    }


    fun fetchAzkarListening(compositeDisposable: CompositeDisposable): LiveData<List<AzkarListeningItem>> {
        quranDataSource= QuranDataSource( apiService,compositeDisposable)
        quranDataSource.fetchAzkarListening()
        return quranDataSource.downloadAzkarListening
    }

//    fun removeLiveData(compositeDisposable: CompositeDisposable){
//        quranDataSource= QuranDataSource( apiService,compositeDisposable)
//        quranDataSource.removeLiveData()
//    }
}