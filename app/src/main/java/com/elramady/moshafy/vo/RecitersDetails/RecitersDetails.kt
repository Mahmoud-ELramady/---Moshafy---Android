package com.elramady.moshafy.vo.RecitersDetails


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

@Entity(tableName = "reciations_table")
data class RecitersDetails(

    @PrimaryKey(autoGenerate = true)
    var number_id:Int,

    val count: String,
    val id: String,
    val letter: String,
    val name: String,
    val rewaya: String,
    @SerializedName("Server")
    val server: String,
    val suras: String,
    val surasData: List<SurasData>
)

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




