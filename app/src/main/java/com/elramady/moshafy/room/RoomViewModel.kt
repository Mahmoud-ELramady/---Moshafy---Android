package com.elramady.moshafy.room

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elramady.moshafy.vo.AzkarListen.AzkarListeningItem
import com.elramady.moshafy.vo.RecitersDetails.RecitersDetails
import com.elramady.moshafy.vo.RecitersNames.Reciter
import com.elramady.moshafy.vo.SurahText.SurahText
import com.elramady.moshafy.vo.SurahsNames.Data
import com.elramady.moshafy.vo.TafseerText.TafseerText
import com.elramady.moshafy.vo.TafseersNames.TafseersNamesItem
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RoomViewModel(var db:DataBase):ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val surahsNamesListDb=MutableLiveData<List<Data>>()

    val surahsAyatListDb=MutableLiveData<SurahText>()
    val checkSurahsAyatInDb=MutableLiveData<Boolean>()


    val recitersNamesListDb=MutableLiveData<List<Reciter>>()
    val searchRecitersNamesList=MutableLiveData<List<Reciter>>()



    val reciationsNames=MutableLiveData<RecitersDetails>()
    val checkReciationInDb=MutableLiveData<Boolean>()


    //Tafseers
    val tafseersNamesListDb=MutableLiveData<List<TafseersNamesItem>>()

    val tafsserAyaDb=MutableLiveData<TafseerText>()
    val checkTafsserAyaInDb=MutableLiveData<Boolean>()

    val azkarListeningListDb=MutableLiveData<List<AzkarListeningItem>>()

    //get list OF surahsNames with specific Ids
    val surahsNamesListWithIdsDb=MutableLiveData<List<Data>>()




    fun insertSurahsNamesList(listNamesDb:List<Data>){

                db.surahsDao.doInsert(listNamesDb)
                .subscribeOn(Schedulers.computation())
                .subscribe(object : CompletableObserver {

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        Log.e("doneInsert","done Insert")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("failInsert",e.toString())
                    }


                })
    }

    fun getSurahsNamesList(){

        try {
            db.surahsDao.getSurahsRoom()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    surahsNamesListDb.postValue(it)
                    Log.e("getSurahs","getSurahs")
                },{

                }
                ).let {
                    compositeDisposable.add(it)
                }
        }catch (e:Exception){
            Log.e("errorLogggg3",e.message.toString())

        }


    }



    fun insertSurahsAyatList(s:SurahText){

        db.surahsDao.doInsertAyat(s)
                .subscribeOn(Schedulers.computation())
                .subscribe(object : CompletableObserver {

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        Log.e("doneInsert","done Insert")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("failInsert",e.toString())
                    }


                })



    }



    fun getSurahsAyatList(id:Int){
        db.surahsDao.getAyatRoom(id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    surahsAyatListDb.postValue(it)
                    Log.e("getSurahsAyat","getSurahsAyat")
                },{

                }
                ).let {
                    compositeDisposable.add(it)
                }
    }


    fun isRowExist(id:Int){
         db.surahsDao.isRowIsExist(id)
             .subscribeOn(Schedulers.computation())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(
                 {
                 checkSurahsAyatInDb.postValue(it)
                 },{

                 }
             ).let {
                 compositeDisposable.add(it)
             }

    }



    fun insertRecirersNamesList(recitersList:List<Reciter>){

        db.surahsDao.doInsertReciters(recitersList)
            .subscribeOn(Schedulers.computation())
            .subscribe(object : CompletableObserver {

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    Log.e("doneInsertReciters","done Insert Reciters")
                }

                override fun onError(e: Throwable) {
                    Log.e("failInsertReciters",e.toString())
                }


            })
    }

    fun getRecitersNamesList(){
        db.surahsDao.getRecitersRoom()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                recitersNamesListDb.postValue(it)
                Log.e("getReciters","getReciters")
            },{

            }
            ).let {
                compositeDisposable.add(it)
            }
    }


    fun searchInReciters(searchReciter:String){

        db.surahsDao.searchNamesReciters(searchReciter)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                        searchRecitersNamesList.postValue(it)
                        },{

                }
                ).let {
                    compositeDisposable.add(it)
                }

    }



    fun insertReciatinsNames(reciatinsNames:RecitersDetails){

        db.surahsDao.doInsertReciations(reciatinsNames)
            .subscribeOn(Schedulers.computation())
            .subscribe(object : CompletableObserver {

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    Log.e("doneInsertReciations","done Insert Reciations")
                }

                override fun onError(e: Throwable) {
                    Log.e("failInsertReciations",e.toString())
                }


            })
    }

    fun getReciationsNames(id:String){
        db.surahsDao.getReciationsRoom(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                reciationsNames.postValue(it)
                Log.e("getReciations","getReciations")
            },{

            }
            ).let {
                compositeDisposable.add(it)
            }
    }



    fun isRowReciationExist(id:String){
        db.surahsDao.isReciationIsExist(id)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    checkReciationInDb.postValue(it)
                    Log.e("done",it.toString())
                },{

                }
            ).let {
                compositeDisposable.add(it)
            }

    }






    //TafseersNames


    fun insertTafseersNamesList(listTafseersNamesDb:List<TafseersNamesItem>){

        db.surahsDao.doInsertTafseersNames(listTafseersNamesDb)
            .subscribeOn(Schedulers.computation())
            .subscribe(object : CompletableObserver {

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    Log.e("doneInsert","done Insert")
                }

                override fun onError(e: Throwable) {
                    Log.e("failInsert",e.toString())
                }


            })
    }

    fun getTafseersNamesList(){
        db.surahsDao.getTafseersNamesRoom()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                tafseersNamesListDb.postValue(it)
                Log.e("getSurahs","getSurahs")
            },{

            }
            ).let {
                compositeDisposable.add(it)
            }
    }



    fun insertTafseerAya(t:TafseerText){

        db.surahsDao.doInsertTafseer(t)
                .subscribeOn(Schedulers.computation())
                .subscribe(object : CompletableObserver {

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        Log.e("doneInsertTafseer","done Insert Tafseer")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("failInsertTafseer",e.toString())
                    }


                })



    }



    fun getTafseerAya(idTafseer:Int,idSurah:Int,idAyah:Int){
        db.surahsDao.getTafseerTextRoom(idTafseer,idSurah,idAyah)
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                 tafsserAyaDb.postValue(it)
                 Log.e("doneGetTafseer","done Get Tafseer")

                },{
                    Log.e("failGetTafseer",it.toString())

                }
                ).let {
                    compositeDisposable.add(it)
                }
    }


    fun isRowTafseerExist(idTafseer:Int,idSurah:Int,idAyah:Int){
        db.surahsDao.isRowTafseerExist(idTafseer,idSurah,idAyah)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            checkTafsserAyaInDb.postValue(it)
                        },{

                }
                ).let {
                    compositeDisposable.add(it)
                }

    }







    fun insertAzkarListeningList(azkarListenDb:List<AzkarListeningItem>){

        db.surahsDao.doInsertAzkarListen(azkarListenDb)
            .subscribeOn(Schedulers.computation())
            .subscribe(object : CompletableObserver {

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    Log.e("doneInsert","done Insert")
                }

                override fun onError(e: Throwable) {
                    Log.e("failInsert",e.toString())
                }


            })
    }

    fun getazkarListeningList(){
        db.surahsDao.getAzkarListenRoom()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                azkarListeningListDb.postValue(it)
                Log.e("getSurahs","getSurahs")
            },{

            }
            ).let {
                compositeDisposable.add(it)
            }
    }












    fun getSurahsNamesListByIds(idsSurhasList:List<Int>){
        db.surahsDao.getSurahsNamesWithIds(idsSurhasList)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                surahsNamesListWithIdsDb.postValue(it)
                Log.e("getSurahs","getSurahs")
            },{

            }
            ).let {
                compositeDisposable.add(it)
            }
    }





    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()

    }




}