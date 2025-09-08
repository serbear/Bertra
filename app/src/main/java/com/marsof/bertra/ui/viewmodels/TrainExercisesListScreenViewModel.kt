package com.marsof.bertra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.data.dao.TrainExerciseDao
import com.marsof.bertra.data.entites.TrainExercise
import com.marsof.bertra.data.entites.TrainExerciseWithExerciseName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


data class TrainExercisesListUiState(
    val trainExercises: List<TrainExercise> = emptyList(),
    val trainExercisesById: List<TrainExercise> = emptyList()
)

class TrainExercisesListScreenViewModel(trainExerciseDao: TrainExerciseDao) : ViewModel() {
    private val _trainId = MutableStateFlow(0L)
    val trainExerciseListUiState: StateFlow<TrainExercisesListUiState> =
        trainExerciseDao.getAllTrainExercises()
            .map { TrainExercisesListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = TrainExercisesListUiState()
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val trainExercisesByIdState: StateFlow<List<TrainExerciseWithExerciseName>> =
        _trainId.flatMapLatest { trainId ->
            if (trainId > 0L) {
                trainExerciseDao.getTrainExercisesById(trainId)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    fun setTrainId(trainId: Long) {
        _trainId.value = trainId
    }
}