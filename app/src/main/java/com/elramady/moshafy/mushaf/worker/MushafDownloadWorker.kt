package com.elramady.moshafy.mushaf.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.elramady.moshafy.mushaf.config.MushafConfig
import com.elramady.moshafy.mushaf.data.local.MushafDatabase
import com.elramady.moshafy.mushaf.data.local.entity.MushafDownloadedPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * WorkManager worker that downloads Mushaf pages in batches.
 * Resumes from last downloaded page. Survives app restart.
 * Batch size: 15 pages per run.
 */
class MushafDownloadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {


    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val dao = MushafDatabase.getInstance(applicationContext).mushafDao()
        val mushafDir = File(applicationContext.filesDir, "mushaf_pages").apply { mkdirs() }

        val lastDownloaded = dao.getLastDownloadedPageNumber() ?: 0
        val startPage = lastDownloaded + 1
        val endPage = minOf(startPage + BATCH_SIZE - 1, MushafConfig.TOTAL_PAGES)

        if (startPage > MushafConfig.TOTAL_PAGES) {
            return@withContext Result.success()
        }

        var downloadedCount = 0
        for (pageNum in startPage..endPage) {
            if (isStopped) break

            val existing = dao.getDownloadedPage(pageNum)
            if (existing != null) {
                if (File(existing.localPath).exists()) continue
            }

            try {
                val url = URL(MushafConfig.getRemotePageUrl(pageNum))
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 30_000
                connection.readTimeout = 30_000
                connection.requestMethod = "GET"

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val file = File(mushafDir, MushafConfig.getLocalFileName(pageNum))
                    connection.inputStream.use { input ->
                        FileOutputStream(file).use { output ->
                            input.copyTo(output)
                        }
                    }
                    dao.insertDownloadedPage(
                        MushafDownloadedPage(
                            pageNumber = pageNum,
                            localPath = file.absolutePath
                        )
                    )
                    downloadedCount++
                }
            } catch (e: Exception) {
                if (downloadedCount == 0) {
                    return@withContext Result.retry()
                }
                break
            }
        }

        if (endPage < MushafConfig.TOTAL_PAGES && downloadedCount > 0) {
            MushafDownloadScheduler.scheduleNextBatch(applicationContext)
        }
        Result.success()
    }

    companion object {
        const val BATCH_SIZE = 15
        const val WORK_NAME = "mushaf_download_work"
    }
}
