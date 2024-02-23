package com.elramady.moshafy.vo.sebha


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "sebha_table")
data class SebhaData(

    @PrimaryKey(autoGenerate = true)
    var id: Int?=null,

    val title: String?,
    val numberOfSebha: Int?,


)