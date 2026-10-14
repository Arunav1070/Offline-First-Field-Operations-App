package com.example.offline_firstfieldoperationsapp.viewmodel

import com.example.offline_firstfieldoperationsapp.data.LocationManager
import com.example.offline_firstfieldoperationsapp.data.SyncManager
import com.example.offline_firstfieldoperationsapp.data.Task
import com.example.offline_firstfieldoperationsapp.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: TaskRepository
    private lateinit var syncManager: SyncManager
    private lateinit var locationManager: LocationManager
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(TaskRepository::class.java)
        syncManager = mock(SyncManager::class.java)
        locationManager = mock(LocationManager::class.java)

        `when`(repository.allTasks).thenReturn(flowOf(emptyList()))

        viewModel = TaskViewModel(repository, syncManager, locationManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun createNewTask_savesToRepositoryAndTriggersSync() = runTest {
        viewModel.createNewTask("Inspect Solar Array", "Check inverter status")
        testDispatcher.scheduler.advanceUntilIdle()

        verify(repository).saveTask(any(Task::class.java) ?: Task(title = "", description = "", status = "", id = ""))
        verify(syncManager).scheduleSync()
    }
}