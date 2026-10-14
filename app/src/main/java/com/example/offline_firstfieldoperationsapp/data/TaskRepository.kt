package com.example.offline_firstfieldoperationsapp.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao)
{
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun saveTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTaskStatusLocally(task: Task, newStatus: String) {
        val updatedTask = task.copy(status = newStatus, syncStatus = SyncStatus.PENDING_UPDATE)
        taskDao.updateTask(updatedTask)
    }
    suspend fun getPendingSyncTasks(): List<Task> {
        return taskDao.getPendingSyncTasks()
    }
}