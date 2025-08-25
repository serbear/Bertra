package com.marsof.bertra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.data.dao.TrainExerciseDao
import com.marsof.bertra.data.entites.TrainExercise
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class TrainExercisesListUiState(val trainExerciseList: List<TrainExercise> = listOf())
class TrainExercisesListScreenViewModel(trainExerciseDao: TrainExerciseDao) : ViewModel() {
    val trainExerciseListUiState: StateFlow<TrainExercisesListUiState> =
        trainExerciseDao.getAllTrainExercises()
            .map { TrainExercisesListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = TrainExercisesListUiState()
            )
}