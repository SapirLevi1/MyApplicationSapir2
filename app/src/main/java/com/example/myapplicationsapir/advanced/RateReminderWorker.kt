package com.example.myapplicationsapir.advanced

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplicationsapir.data.local.database.MovieDatabase

class RateReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): androidx.work.ListenableWorker.Result {
        val dao = MovieDatabase.getDatabase(applicationContext).movieDao()
        val unratedCount = dao.countUnratedMovies()

        if (unratedCount > 0) {
            NotificationHelper.showRateReminder(applicationContext, unratedCount)
        }

        return androidx.work.ListenableWorker.Result.success()
    }
}
