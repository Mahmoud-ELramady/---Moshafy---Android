package com.elramady.moshafy.mushaf.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mushaf_downloaded_pages")
data class MushafDownloadedPage(
    @PrimaryKey val pageNumber: Int,
    val localPath: String,
    val downloadedAt: Long = System.currentTimeMillis()
)
