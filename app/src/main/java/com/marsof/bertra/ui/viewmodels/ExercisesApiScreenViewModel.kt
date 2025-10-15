package com.marsof.bertra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.data.repository.IMusclesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException

class ExercisesApiScreenViewModel(
    private val musclesRepository: IMusclesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ExercisesApiScreenUiState>(
        ExercisesApiScreenUiState.Loading
    )
    val uiState: StateFlow<ExercisesApiScreenUiState> = _uiState.asStateFlow()

    init {
        getMuscles()
    }

    fun getMuscles() {
        viewModelScope.launch {
            _uiState.value = ExercisesApiScreenUiState.Loading
            musclesRepository.getMusclesStream()
                .catch { exception ->
                    // Перехватываем ошибки из Flow
                    val errorMessage = when (exception) {
                        is IOException -> "Network error occurred."
                        else -> "An unexpected error occurred."
                    }
                    _uiState.value = ExercisesApiScreenUiState.Error(errorMessage)
                }
                .collect { muscles ->
                    // При успешном получении данных обновляем состояние
                    _uiState.value = ExercisesApiScreenUiState.Success(muscles)
                }
        }
    }
}