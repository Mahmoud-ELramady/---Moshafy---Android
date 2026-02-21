package com.elramady.moshafy.mushaf.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mushaf_last_read")
data class MushafLastReadPage(
    @PrimaryKey val id: Int = 1,
    val pageNumber: Int,
    val timestamp: Long = System.currentTimeMillis()
)
