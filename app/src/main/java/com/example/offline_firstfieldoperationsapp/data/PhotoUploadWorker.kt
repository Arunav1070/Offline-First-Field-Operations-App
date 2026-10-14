package com.example.offline_firstfieldoperationsapp.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.concurrent.TimeUnit

@HiltWorker
class PhotoUploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val apiService: ApiService
) : CoroutineWorker(context,params){

    override suspend fun doWork(): Result{
        val photoPath = inputData.getString("PHOTO_PATH") ?: return Result.failure()
        val taskId = inputData.getString("TASK_ID") ?: return Result.failure()
        val file = File(photoPath)
        if(!file.exists()) return  Result.failure()
         return try {
             val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
             val body = MultipartBody.Part.createFormData("photo", file.name, requestFile)

             val response = apiService.uploadTaskPhoto(taskId, body)
             if (response.isSuccessful){
                 Result.success()
             } else {
                 Result.retry()
             }
         } catch (e: Exception) {
             Result.retry()
         }
    }

    companion object {
        fun enqueuePhotoUpload(context: Context, taskId: String, photoPath: String){
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build()

            val uploadRequest = OneTimeWorkRequestBuilder<PhotoUploadWorker>()
                .setConstraints(constraints)
                .setInputData(workDataOf("Task_ID" to taskId, "PHOTO_PATH" to photoPath))
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
                .build()

            WorkManager.getInstance(context).enqueue(uploadRequest)
        }
    }
}



