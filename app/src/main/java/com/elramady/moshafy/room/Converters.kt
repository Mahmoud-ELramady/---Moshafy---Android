package com.elramady.moshafy.room

import androidx.room.TypeConverter
import com.elramady.moshafy.vo.SurahText.Verse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromListToString(list: List<Verse>):String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToList(value:String?):List<Verse>{
        val list=object :TypeToken<List<Verse>>(){}.type
        return Gson().fromJson(value,list)
    }






}