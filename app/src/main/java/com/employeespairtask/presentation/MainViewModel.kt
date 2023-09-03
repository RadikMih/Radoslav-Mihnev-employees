package com.employeespairtask.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.employeespairtask.data.repository.employee.EmployeePairRepository
import com.employeespairtask.model.Team
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val employeePairRepository: EmployeePairRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.InitialState)
    val state = _state.asStateFlow()

    fun getTopTeam(uri: Uri) {
        _state.update {
            UiState.Loading
        }

        viewModelScope.launch {
            val topTeamResult = employeePairRepository.getEmployeeTopPair(uri)
            topTeamResult.fold(
                onSuccess = { team ->
                    _state.update {
                        UiState.TopTeam(team)
                    }
                },
                onFailure = { error ->
                    _state.update {
                        UiState.ErrorInfo(error.message ?: "")
                    }
                }
            )
        }
    }

    sealed class UiState {
        object InitialState : UiState()
        data class TopTeam(val team: Team) : UiState()
        object Loading : UiState()
        data class ErrorInfo(val info: String) : UiState()
    }
}