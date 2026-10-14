package com.example.offline_firstfieldoperationsapp.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Multipart
import retrofit2.http.Path
import retrofit2.http.Part
import okhttp3.MultipartBody

interface ApiService{
    @POST("api/v1/tasks/sync")
    suspend fun syncTasks(@Body tasks: List<Task>): Response<Unit>

    @Multipart
    @POST("api/v1/tasks/{id}/photo")
    suspend fun uploadTaskPhoto(
        @Path("id") taskId: String,
        @Part photo: MultipartBody.Part
    ): Response<Unit>
}

