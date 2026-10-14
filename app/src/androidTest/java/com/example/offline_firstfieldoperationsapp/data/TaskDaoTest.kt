package com.example.offline_firstfieldoperationsapp.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var taskDao: TaskDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        taskDao = database.taskDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndFetchOfflineTask() = runTest {
        val task = Task(
            id = UUID.randomUUID().toString(),
            title = "Inspect Field",
            description = "Verify solar panel status",
            status = "PENDING",
            syncStatus = SyncStatus.PENDING_UPDATE
        )

        taskDao.insertTask(task)
        val tasks = taskDao.getAllTasks().first()

        assertEquals(1, tasks.size)
        assertEquals("Inspect Field", tasks[0].title)
        assertEquals(SyncStatus.PENDING_UPDATE, tasks[0].syncStatus)
    }
}
