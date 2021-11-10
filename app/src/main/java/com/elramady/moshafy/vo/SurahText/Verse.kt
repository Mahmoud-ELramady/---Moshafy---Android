package com.elramady.moshafy.vo.SurahText


import com.google.gson.annotations.SerializedName

data class Verse(
    val number: Int,
    val text: String,
    @SerializedName("translation_en")
    val translationEn: String,
    @SerializedName("translation_id")
    val translationId: String
)