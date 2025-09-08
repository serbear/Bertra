package com.marsof.bertra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.data.dao.TrainExerciseDao
import com.marsof.bertra.data.entites.TrainExercise
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

data class WorkoutEngageUiState(
    val workoutExercises: List<TrainExercise> = emptyList(),
)

class WorkoutEngageScreenViewModel(workoutExerciseDao: TrainExerciseDao) : ViewModel() {
    private val _workoutId = MutableStateFlow(0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val workoutExercisesByIdState: StateFlow<List<TrainExercise>> =
        _workoutId.flatMapLatest { workoutId ->
            if (workoutId > 0L) {
                workoutExerciseDao.getTrainExercisesById(workoutId)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    fun setTrainId(workoutId: Long) {
        _workoutId.value = workoutId
    }
}