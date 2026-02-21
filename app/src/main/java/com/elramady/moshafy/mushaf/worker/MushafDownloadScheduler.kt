package com.elramady.moshafy.mushaf.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

/**
 * Schedules Mushaf page downloads.
 * Call on first app open to start progressive download.
 */
object MushafDownloadScheduler {

    fun scheduleDownload(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<MushafDownloadWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                androidx.work.BackoffPolicy.EXPONENTIAL,
                30,
                TimeUnit.SECONDS
            )
            .addTag(WORK_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            MushafDownloadWorker.WORK_NAME,
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }

    fun scheduleNextBatch(context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<MushafDownloadWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .addTag(WORK_TAG)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private const val WORK_TAG = "mushaf_download"
}
