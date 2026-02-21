package com.elramady.moshafy.mushaf.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mushaf_bookmarks")
data class MushafBookmark(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pageNumber: Int,
    val timestamp: Long = System.currentTimeMillis()
)
