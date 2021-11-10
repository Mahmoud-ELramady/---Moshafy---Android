package com.elramady.moshafy.vo.TafseersNames


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tafseers_names_table")
data class TafseersNamesItem(
    val author: String,
    @SerializedName("book_name")
    val bookName: String,

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val language: String,
    val name: String
)