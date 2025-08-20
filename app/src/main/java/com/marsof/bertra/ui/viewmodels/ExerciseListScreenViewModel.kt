package com.marsof.bertra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.data.Exercise
import com.marsof.bertra.data.ExerciseDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ExerciseListUiState(val exerciseList: List<Exercise> = listOf())
class ExerciseListScreenViewModel(exerciseDao: ExerciseDao) : ViewModel() {
    val exerciseListUiState: StateFlow<ExerciseListUiState> = exerciseDao.getAllExercises()
        .map { ExerciseListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ExerciseListUiState()
        )
}