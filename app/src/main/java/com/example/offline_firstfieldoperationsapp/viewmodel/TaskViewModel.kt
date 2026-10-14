package com.example.offline_firstfieldoperationsapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offline_firstfieldoperationsapp.data.SyncManager
import com.example.offline_firstfieldoperationsapp.data.Task
import com.example.offline_firstfieldoperationsapp.data.TaskRepository
import com.example.offline_firstfieldoperationsapp.data.LocationManager
import com.example.offline_firstfieldoperationsapp.data.PhotoUploadWorker
import com.example.offline_firstfieldoperationsapp.data.SyncStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val syncManager: SyncManager,
    private val locationManager: LocationManager
) : ViewModel() {
    val tasks: StateFlow<List<Task>> = repository.allTasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    fun createNewTask(title: String, description: String) {
        viewModelScope.launch{
            val newTask = Task(
                id = UUID.randomUUID().toString(),
                title = title,
                description = description,
                status = "ASSIGNED",
                syncStatus = SyncStatus.PENDING_UPDATE,
                updatedAt = System.currentTimeMillis())
            repository.saveTask(newTask)
            syncManager.scheduleSync()
        }
    }
    fun updateTaskWithLocationAndPhoto(
        context: Context,
        task: Task,
        newStatus: String,
        photoPath: String? = null
    ) {
        viewModelScope.launch {
            val location = locationManager.getCurrentLocation()
            val updatedTask = task.copy(
                status = newStatus,
                latitude = location?.latitude ?: task.latitude,
                longitude = location?.longitude ?: task.longitude,
                photoPath = photoPath ?: task.photoPath,
                syncStatus = SyncStatus.SYNCED,
                updatedAt = System.currentTimeMillis()
            )

            repository.updateTaskStatusLocally(updatedTask, newStatus)

            photoPath?.let { path ->
                PhotoUploadWorker.enqueuePhotoUpload(context, task.id, path)
            }

            syncManager.scheduleSync()
        }
    }
    fun triggerManualSync(){
        syncManager.scheduleSync()
    }
    fun forceSyncAllTasks(){
        viewModelScope.launch {
            val pending = repository.getPendingSyncTasks()
            pending.forEach { task ->
                val syncedTask = task.copy(syncStatus = SyncStatus.SYNCED)
                repository.saveTask(syncedTask)
            }
        }
    }
}