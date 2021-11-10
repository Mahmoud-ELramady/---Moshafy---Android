package com.elramady.moshafy.Api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.alquran.cloud/v1/"
const val BASE_URL_TEXT = "https://unpkg.com/"
const val BASE_URL_RECITERS = "http://mp3quran.net/"
const val BASE_URL_RECIATIONS = "https://qurani-api.herokuapp.com/api/"
const val BASE_URL_TAFSEERS = "http://api.quran-tafseer.com/"
const val BASE_URL_AZKAR = "https://gad25.xyz/Quran/"

object SwarClient {


    fun getSwarClient(): SwarInterface {

        val interceptor:HttpLoggingInterceptor= HttpLoggingInterceptor()
        interceptor.level=HttpLoggingInterceptor.Level.BODY


        val okHttpClient= OkHttpClient.Builder()
            .addInterceptor(interceptor)
                .build()


      return Retrofit.Builder()
              .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(SwarInterface::class.java)
    }



    fun getSurahTextClient(): SwarInterface {

        val interceptor:HttpLoggingInterceptor= HttpLoggingInterceptor()
        interceptor.level=HttpLoggingInterceptor.Level.BODY


        val okHttpClient= OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()


        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL_TEXT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(SwarInterface::class.java)
    }




//
    fun getRecitersClient():SwarInterface{
        val interceptor:HttpLoggingInterceptor= HttpLoggingInterceptor()
        interceptor.level=HttpLoggingInterceptor.Level.BODY


        val okHttpClient= OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()


        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL_RECITERS)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(SwarInterface::class.java)



    }

    fun getReciationsClient():SwarInterface{

        val interceptor:HttpLoggingInterceptor= HttpLoggingInterceptor()
        interceptor.level=HttpLoggingInterceptor.Level.BODY

        val okHttpClient= OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL_RECIATIONS)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(SwarInterface::class.java)



    }




    fun getTafseersClient():SwarInterface{

        val interceptor:HttpLoggingInterceptor= HttpLoggingInterceptor()
        interceptor.level=HttpLoggingInterceptor.Level.BODY

        val okHttpClient= OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL_TAFSEERS)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(SwarInterface::class.java)



    }




    fun getAzkarClient():SwarInterface{

        val interceptor:HttpLoggingInterceptor= HttpLoggingInterceptor()
        interceptor.level=HttpLoggingInterceptor.Level.BODY

        val okHttpClient= OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL_AZKAR)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(SwarInterface::class.java)



    }

}