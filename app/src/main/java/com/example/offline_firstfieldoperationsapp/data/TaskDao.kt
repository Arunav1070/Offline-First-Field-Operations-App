package com.example.offline_firstfieldoperationsapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
@Query("SELECT * FROM tasks ORDER BY updatedAt DESC")
fun getAllTasks(): Flow<List<Task>>

@Query("Select * FROM tasks WHERE syncStatus != 'SYNCED'")
suspend fun getPendingSyncTasks(): List<Task>

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertTask(task: Task)

@Update
suspend fun updateTask(task: Task)
}
