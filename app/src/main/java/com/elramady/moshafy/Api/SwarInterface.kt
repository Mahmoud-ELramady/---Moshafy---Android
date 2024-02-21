package com.elramady.moshafy.Api

import com.elramady.moshafy.vo.AzkarListen.AzkarListeningItem
import com.elramady.moshafy.vo.RecitersDetails.RecitersDetails
import com.elramady.moshafy.vo.RecitersNames.Reciters
import com.elramady.moshafy.vo.SurahText.SurahText
import com.elramady.moshafy.vo.SurahsNames.Surahs
import com.elramady.moshafy.vo.TafseerText.TafseerText
import com.elramady.moshafy.vo.TafseersNames.TafseersNamesItem
import io.reactivex.rxjava3.core.Observable

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

//http://api.alquran.cloud/v1/surah/114

// for reciters http://mp3quran.net/api/_arabic.php

//https://qurani-api.herokuapp.com/api/reciters/1

//https://unpkg.com/quran-json@1.0.1/json/surahs/1.json


// http://api.quran-tafseer.com/tafseer/

//tafseer.com/tafseer/{tafseer_id}/{sura_number}/{ayah_number_from}/{ayah_number_to}
//http://api.quran-tafseer.com/tafseer/2/2/1/15


//api.quran-tafseer.com/tafseer/{tafseer_id}/{sura_number}/{ayah_number}

//https://newsapi.org/v2/top-headlines?country=us&apiKey=da29f2094590433f9c081707357f56ce
interface SwarInterface {

    @GET("surah")
    fun getSurhasNames(): Single<Surahs>


    @GET("quran-json@1.0.1/json/surahs/{surah_id}.json")
    fun getSurahsText(@Path("surah_id") id:Int):Single<SurahText>




    @GET("api/_arabic.php")
    fun getRecitersNames(): Single<Reciters>




    @GET("reciters/{reciter_id}")
    fun getRecitations(@Path("reciter_id") reciterId:String): Single<RecitersDetails>




    @GET("tafseer/")
    fun getTafseersNames():Single<List<TafseersNamesItem>>



    @GET("tafseer/{tafseer_id}/{sura_number}/{ayah_number}")
    fun getTafseerTextAyah(@Path("tafseer_id")tafseer_id:Int,
                       @Path("sura_number")sura_number:Int,
                       @Path("ayah_number") ayah_number:Int):Observable<TafseerText>



    @GET("Azkar.php")
    fun getAzkarListening():Single<List<AzkarListeningItem>>





}




