package com.elramady.moshafy.mushaf.data.repository

import android.content.Context
import com.elramady.moshafy.mushaf.config.MushafConfig
import com.elramady.moshafy.mushaf.data.local.MushafDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Repository responsible for loading Mushaf page images.
 * Loads from local storage first. If not available, provides remote URL.
 * Never downloads the same page twice (download is handled by WorkManager).
 */
class MushafImageRepository(
    private val context: Context,
    private val mushafDao: MushafDao
) {
    private val mushafDir: File by lazy {
        File(context.filesDir, "mushaf_pages").apply { mkdirs() }
    }

    /**
     * Returns the image source for the given page:
     * - File path if downloaded locally
     * - Remote URL if not yet downloaded
     */
    suspend fun getPageImageSource(pageNumber: Int): ImageSource = withContext(Dispatchers.IO) {
        require(pageNumber in 1..MushafConfig.TOTAL_PAGES) { "Invalid page: $pageNumber" }
        val localPath = mushafDao.getDownloadedPage(pageNumber)?.localPath
        if (localPath != null) {
            val file = File(localPath)
            if (file.exists()) {
                return@withContext ImageSource.Local(file)
            }
        }
        ImageSource.Remote(MushafConfig.getRemotePageUrl(pageNumber))
    }

    fun getLocalFileForPage(pageNumber: Int): File =
        File(mushafDir, MushafConfig.getLocalFileName(pageNumber))

    suspend fun isPageDownloaded(pageNumber: Int): Boolean = withContext(Dispatchers.IO) {
        mushafDao.getDownloadedPage(pageNumber)?.let { page ->
            File(page.localPath).exists()
        } ?: false
    }

    suspend fun getDownloadedCount(): Int = mushafDao.getDownloadedPagesCount()

    suspend fun getLastDownloadedPage(): Int? = mushafDao.getLastDownloadedPageNumber()

    sealed class ImageSource {
        data class Local(val file: File) : ImageSource()
        data class Remote(val url: String) : ImageSource()
    }
}
