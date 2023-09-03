package com.employeespairtask

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.employeespairtask.presentation.HomeScreen
import com.employeespairtask.presentation.InfoScreen
import com.employeespairtask.presentation.MainViewModel
import com.employeespairtask.presentation.ResultScreen
import com.employeespairtask.ui.theme.EmployeesPairTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var getContentLauncher: ActivityResultLauncher<String>? = null
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getContentLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let { viewModel.getTopTeam(it) }
            }

        setContent {
            EmployeesPairTaskTheme {
                AppScreen { getContentLauncher?.launch("text/csv") }
            }
        }
    }
}

@Composable
fun AppScreen(mainViewModel: MainViewModel = viewModel(), onClick: () -> Unit) {
    val viewModelState by mainViewModel.state.collectAsStateWithLifecycle()

    when (val state = viewModelState) {
        MainViewModel.UiState.InitialState -> {
            HomeScreen { onClick() }
        }

        is MainViewModel.UiState.TopTeam -> {
            ResultScreen(team = state.team)
        }

        is MainViewModel.UiState.ErrorInfo -> {
            InfoScreen(info = state.info)
        }

        MainViewModel.UiState.Loading -> {}
    }
}