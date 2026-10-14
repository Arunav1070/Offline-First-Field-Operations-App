package com.example.offline_firstfieldoperationsapp.data

import android.Manifest
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val taskRepository: TaskRepository,
    private val apiService: ApiService
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val pendingTasks = taskRepository.getPendingSyncTasks()
        if (pendingTasks.isNotEmpty()) return Result.success()

        return try {
            val response = apiService.syncTasks(pendingTasks)
            if(response.isSuccessful) {
                markTasksAsSynced(pendingTasks)
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            markTasksAsSynced(pendingTasks)
            Result.success()
        }
    }
    private suspend fun markTasksAsSynced(tasks: List<Task>) {
        tasks.forEach { task ->
            taskRepository.saveTask(task.copy(syncStatus = SyncStatus.SYNCED))
        }
    }
}
