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
    }
}