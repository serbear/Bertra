package com.marsof.bertra.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.data.Muscle
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
    private val _uiStateExercises = MutableStateFlow<ExercisesApiScreenUiState>(
        ExercisesApiScreenUiState.Loading(message = "")
    )
    val uiStateExercises: StateFlow<ExercisesApiScreenUiState> = _uiStateExercises.asStateFlow()

    private val _uiStateMuscles = MutableStateFlow<ExercisesApiScreenUiState>(
        ExercisesApiScreenUiState.Loading(message = "")
    )
    val uiStateMuscles: StateFlow<ExercisesApiScreenUiState> = _uiStateMuscles.asStateFlow()

    init {
        getMuscles()
    }

    fun getMuscles() {
        viewModelScope.launch {
            _uiStateMuscles.value = ExercisesApiScreenUiState.Loading(
                message = "Loading muscle list"
            )
            musclesRepository.getMusclesStream()
                .catch { exception ->
                    // Get an error from Flow.
                    val errorMessage = when (exception) {
                        is IOException -> "Network error occurred."
                        else -> "An unexpected error occurred."
                    }
                    _uiStateMuscles.value = ExercisesApiScreenUiState.Error(errorMessage)
                }
                .collect { muscles ->
                    // Update state on successful data retrieval.
                    _uiStateMuscles.value = ExercisesApiScreenUiState.Success(muscles)
                }
        }
    }

    fun getExercisesFor(muscle: Muscle) {
        Log.d("BERTRA", "getExercisesFor: ${muscle.name}")

        viewModelScope.launch {
            _uiStateExercises.value = ExercisesApiScreenUiState.Loading(
                message = "Loading exercises for muscle ${muscle.name}"
            )
            musclesRepository.getExerciseForMuscleStream(muscle)
                .catch { exception ->
                    // Get an error from Flow.
                    val errorMessage = when (exception) {
                        is IOException -> "Network error occurred."
                        else -> "An unexpected error occurred."
                    }
                    _uiStateExercises.value = ExercisesApiScreenUiState.Error(errorMessage)
                }
                .collect { exercises ->
                    // Update state on successful data retrieval.
                    _uiStateExercises.value = ExercisesApiScreenUiState.Success(exercises)
                }
        }
    }
}