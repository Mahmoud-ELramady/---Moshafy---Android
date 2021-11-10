package com.elramady.moshafy.vo.SurahsNames

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "surhas_names_table")
data class Data(
    val englishName: String,
    val englishNameTranslation: String,
    val name: String,
    @PrimaryKey(autoGenerate = true)
    var number: Int,

    val numberOfAyahs: Int,
    val revelationType: String
)