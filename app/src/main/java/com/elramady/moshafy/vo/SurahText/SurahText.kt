package com.elramady.moshafy.vo.SurahText


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "surhas_ayat_table")
data class SurahText(
    val name: String,

    @PrimaryKey(autoGenerate = true)
    var number: Int,
    
    @SerializedName("revelation_type")
    val revelationType: String,
    @SerializedName("total_verses")
    val totalVerses: Int,
    @SerializedName("translation_en")
    val translationEn: String,
    @SerializedName("transliteration_en")
    val transliterationEn: String,


    val verses: List<Verse>
)