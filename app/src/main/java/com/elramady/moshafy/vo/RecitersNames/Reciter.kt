package com.elramady.moshafy.vo.RecitersNames


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "reciters_table")
data class Reciter(

    @PrimaryKey(autoGenerate = true)
    var number:Int,

    val count: String,
    val id: String,
    val letter: String,
    val name: String,
    val rewaya: String,
    @SerializedName("Server")
    val server: String,
    val suras: String
)