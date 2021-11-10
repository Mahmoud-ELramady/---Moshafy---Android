package com.elramady.moshafy.vo.AzkarListen


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "azkar_listen_table")
data class AzkarListeningItem(

    @PrimaryKey(autoGenerate = true)
    var id:Int,

    val link: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("reader_img")
    val readerImg: String,
    @SerializedName("reader_name")
    val readerName: String
)