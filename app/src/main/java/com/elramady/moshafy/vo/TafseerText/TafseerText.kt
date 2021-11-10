package com.elramady.moshafy.vo.TafseerText


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tafseer_ayat_table")
data class TafseerText(
    @PrimaryKey(autoGenerate = true)
    var id:Int,

    var surah_id:Int,

    @SerializedName("ayah_number")
    val ayahNumber: Int,

    @SerializedName("ayah_url")
    val ayahUrl: String,

    @SerializedName("tafseer_id")
    val tafseerId: Int,

    @SerializedName("tafseer_name")
    val tafseerName: String,

    val text: String
)