package com.elramady.moshafy.mushaf.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elramady.moshafy.mushaf.data.local.entity.MushafBookmark
import com.elramady.moshafy.mushaf.data.local.entity.MushafDownloadedPage
import com.elramady.moshafy.mushaf.data.local.entity.MushafLastReadPage

@Database(
    entities = [
        MushafDownloadedPage::class,
        MushafBookmark::class,
        MushafLastReadPage::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MushafDatabase : RoomDatabase() {
    abstract fun mushafDao(): MushafDao

    companion object {
        private const val DATABASE_NAME = "mushaf_database"

        @Volatile
        private var instance: MushafDatabase? = null

        fun getInstance(context: Context): MushafDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    MushafDatabase::class.java,
                    DATABASE_NAME
                ).build().also { instance = it }
            }
        }
    }
}
