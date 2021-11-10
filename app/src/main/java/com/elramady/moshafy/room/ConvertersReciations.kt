package com.elramady.moshafy.room

import androidx.room.TypeConverter
import com.elramady.moshafy.vo.RecitersDetails.SurasData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConvertersReciations {

        @TypeConverter
        fun fromListToStringg(list: List<SurasData>):String{
            return Gson().toJson(list)
        }

        @TypeConverter
        fun fromStringToListt(value:String?):List<SurasData>{
            val list=object : TypeToken<List<SurasData>>(){}.type
            return Gson().fromJson(value,list)
        }





}