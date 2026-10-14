package com.example.offline_firstfieldoperationsapp.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManager @Inject constructor(@ApplicationContext private val context: Context) {
    fun scheduleSync(){
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "SyncTasksWork",
            ExistingWorkPolicy.REPLACE,
            syncRequest)
    }
    }
