package com.example.offline_firstfieldoperationsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import com.example.offline_firstfieldoperationsapp.data.TokenManager
import com.example.offline_firstfieldoperationsapp.ui.LoginScreen
import javax.inject.Inject
import com.example.offline_firstfieldoperationsapp.ui.theme.OfflineFirstFieldOperationsAppTheme
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.offline_firstfieldoperationsapp.data.NetworkMonitor
import com.example.offline_firstfieldoperationsapp.data.SyncManager
import com.example.offline_firstfieldoperationsapp.ui.TaskListScreen
import com.example.offline_firstfieldoperationsapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var  tokenManager: TokenManager
    @Inject lateinit var networkMonitor: NetworkMonitor
    @Inject lateinit var syncManager: SyncManager
 private val taskViewModel: TaskViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            networkMonitor.isOnline.collect{ isConnected ->
                if(isConnected && tokenManager.getAuthToken().isNullOrEmpty()){
                    syncManager.scheduleSync()
                }
            }
        }
        setContent {
            var isAuthenticated by remember { mutableStateOf(!tokenManager.getAuthToken().isNullOrEmpty())
            }
            if (isAuthenticated) {
                TaskListScreen(viewModel = taskViewModel,
                    onLogout = {
                    tokenManager.clearSession()
                        isAuthenticated = false
                }
                )

            } else {
                LoginScreen(onLoginSuccess = { token ->
                    tokenManager.saveAuthToken(token)
                    isAuthenticated = true
                }
                )
                }
            }
        }
    }
