package com.elramady.moshafy.mushaf.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elramady.moshafy.mushaf.data.local.entity.MushafBookmark
import com.elramady.moshafy.mushaf.data.local.entity.MushafDownloadedPage
import com.elramady.moshafy.mushaf.data.local.entity.MushafLastReadPage
import kotlinx.coroutines.flow.Flow

@Dao
interface MushafDao {

    // Downloaded pages
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownloadedPage(page: MushafDownloadedPage)

    @Query("SELECT * FROM mushaf_downloaded_pages WHERE pageNumber = :pageNumber")
    suspend fun getDownloadedPage(pageNumber: Int): MushafDownloadedPage?

    @Query("SELECT pageNumber FROM mushaf_downloaded_pages ORDER BY pageNumber")
    suspend fun getAllDownloadedPageNumbers(): List<Int>

    @Query("SELECT COUNT(*) FROM mushaf_downloaded_pages")
    suspend fun getDownloadedPagesCount(): Int

    @Query("SELECT COUNT(*) FROM mushaf_downloaded_pages")
    fun getDownloadedPagesCountFlow(): Flow<Int>

    @Query("SELECT MAX(pageNumber) FROM mushaf_downloaded_pages")
    suspend fun getLastDownloadedPageNumber(): Int?

    // Bookmarks
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: MushafBookmark)

    @Query("DELETE FROM mushaf_bookmarks WHERE pageNumber = :pageNumber")
    suspend fun deleteBookmarkByPage(pageNumber: Int)

    @Query("SELECT * FROM mushaf_bookmarks WHERE pageNumber = :pageNumber")
    suspend fun getBookmarkByPage(pageNumber: Int): MushafBookmark?

    @Query("SELECT * FROM mushaf_bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<MushafBookmark>>

    @Query("SELECT * FROM mushaf_bookmarks ORDER BY timestamp DESC")
    suspend fun getAllBookmarksSync(): List<MushafBookmark>

    // Last read page
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLastReadPage(lastRead: MushafLastReadPage)

    @Query("SELECT * FROM mushaf_last_read WHERE id = 1")
    suspend fun getLastReadPage(): MushafLastReadPage?

    @Query("SELECT pageNumber FROM mushaf_last_read WHERE id = 1")
    suspend fun getLastReadPageNumber(): Int?
}
