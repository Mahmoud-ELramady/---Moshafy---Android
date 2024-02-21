package com.elramady.moshafy.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elramady.moshafy.vo.AzkarListen.AzkarListeningItem
import com.elramady.moshafy.vo.RecitersDetails.RecitersDetails
import com.elramady.moshafy.vo.RecitersNames.Reciter
import com.elramady.moshafy.vo.SurahText.SurahText
import com.elramady.moshafy.vo.SurahsNames.Data
import com.elramady.moshafy.vo.TafseerText.TafseerText
import com.elramady.moshafy.vo.TafseersNames.TafseersNamesItem
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface SurahsNamesDao {

    @Insert
    fun doInsert(d:List<Data>):Completable

    @Query("select * from surhas_names_table")
    fun getSurahsRoom(): Single<List<Data>>

    @Query("select * from surhas_names_table where number IN (:idsList)")
    fun getSurahsNamesWithIds(idsList:List<Int>): Single<List<Data>>

    

//    //check if table is exist
//    @Query("SELECT EXISTS(SELECT * FROM surhas_names_table)")
//    fun isExistSurhasNamesTable() : Observable<Boolean>




    @Insert
    fun doInsertAyat(s:SurahText):Completable

    @Query("select * from surhas_ayat_table where number= :id")
    fun getAyatRoom(id:Int): Single<SurahText>

    @Query("SELECT EXISTS(SELECT * FROM surhas_ayat_table WHERE  number= :id)")
    fun isRowIsExist(id : Int) : Observable<Boolean>




    //Reciters Names

    @Insert
    fun doInsertReciters(reciters:List<Reciter>):Completable

    @Query("select * from reciters_table")
    fun getRecitersRoom(): Single<List<Reciter>>
//
    @Query("SELECT * FROM reciters_table WHERE name LIKE :searchQuery OR rewaya LIKE :searchQuery")
    fun searchNamesReciters(searchQuery:String):Single<List<Reciter>>





    //Reciations
    @Insert
    fun doInsertReciations(reciations:RecitersDetails):Completable

    @Query("select * from reciations_table where id= :id")
    fun getReciationsRoom(id:String): Single<RecitersDetails>

    @Query("SELECT EXISTS(SELECT * FROM reciations_table WHERE  id= :id)")
    fun isReciationIsExist(id : String) : Observable<Boolean>



    //Tafseers
    @Insert
    fun doInsertTafseersNames(tafseersNamesList:List<TafseersNamesItem>):Completable

    @Query("SELECT * FROM tafseers_names_table")
    fun getTafseersNamesRoom():Single<List<TafseersNamesItem>>



    @Insert
    fun doInsertTafseer(t:TafseerText):Completable

    @Query("select * from tafseer_ayat_table where tafseerId= :idTafseer and surah_id= :idSurah and ayahNumber= :idAyah")
    fun getTafseerTextRoom(idTafseer:Int,idSurah:Int,idAyah:Int): Single<TafseerText>

    @Query("SELECT EXISTS(SELECT * FROM tafseer_ayat_table where tafseerId= :idTafseer and surah_id= :idSurah and ayahNumber= :idAyah)")
    fun isRowTafseerExist(idTafseer:Int,idSurah:Int,idAyah:Int) : Observable<Boolean>



    //Tafseers
    @Insert
    fun doInsertAzkarListen(azkarListenList:List<AzkarListeningItem>):Completable

    @Query("SELECT * FROM azkar_listen_table")
    fun getAzkarListenRoom():Single<List<AzkarListeningItem>>

}