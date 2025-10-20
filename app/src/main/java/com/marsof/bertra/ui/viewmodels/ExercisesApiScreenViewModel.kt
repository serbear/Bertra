package com.marsof.bertra.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.data.Muscle
import com.marsof.bertra.data.repository.IMusclesRepository
import com.marsof.bertra.ui.screens.ExercisesApiScreen
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
        ExercisesApiScreenUiState.Loading(message = "")
    )
    val uiState: StateFlow<ExercisesApiScreenUiState> = _uiState.asStateFlow()

    init {
        getMuscles()
    }

    fun getMuscles() {
        viewModelScope.launch {
            _uiState.value = ExercisesApiScreenUiState.Loading(message = "Loading muscle list")
            musclesRepository.getMusclesStream()
                .catch { exception ->
                    // Get an error from Flow.
                    val errorMessage = when (exception) {
                        is IOException -> "Network error occurred."
                        else -> "An unexpected error occurred."
                    }
                    _uiState.value = ExercisesApiScreenUiState.Error(errorMessage)
                }
                .collect { muscles ->
                    // Update state on successful data retrieval.
                    _uiState.value = ExercisesApiScreenUiState.Success(muscles)
                }
        }
    }

    fun getExercisesFor(muscle: Muscle){
        Log.d("BERTRA", "getExercisesFor: ${muscle.name}")

        viewModelScope.launch {
            _uiState.value = ExercisesApiScreenUiState.Loading(
                message = "Loading exercises for muscle ${muscle.name}"
            )
            musclesRepository.getExerciseForMuscleStream(muscle)
                .catch { exception ->
                    // Get an error from Flow.
                    val errorMessage = when (exception) {
                        is IOException -> "Network error occurred."
                        else -> "An unexpected error occurred."
                    }
                    _uiState.value = ExercisesApiScreenUiState.Error(errorMessage)
                }
                .collect { exercises ->
                    // Update state on successful data retrieval.
                    _uiState.value = ExercisesApiScreenUiState.Success(exercises)
                }
        }
    }
}