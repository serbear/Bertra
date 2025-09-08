package com.marsof.bertra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.data.dao.TrainDao
import com.marsof.bertra.data.dao.TrainExerciseDao
import com.marsof.bertra.data.entites.Train
import com.marsof.bertra.data.entites.TrainExercise
import com.marsof.bertra.data.entites.TrainExerciseWithExerciseName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

data class WorkoutEngageUiState(
    val workoutExercises: List<TrainExercise> = emptyList(),
)

class WorkoutEngageScreenViewModel(
    workoutExerciseDao: TrainExerciseDao,
    workoutDao : TrainDao
) : ViewModel() {
    private val _workoutId = MutableStateFlow(0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val workoutExercisesByIdState: StateFlow<List<TrainExerciseWithExerciseName>> =
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val workoutState: StateFlow<Train?> =
        _workoutId.flatMapLatest { workoutId ->
            if (workoutId > 0L) {
                workoutDao.getTrain(_workoutId.value)
            } else {
                flowOf(null)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = null
        )


    fun setWorkoutId(workoutId: Long) {
        _workoutId.value = workoutId
    }
}