package com.marsof.bertra.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.data.dao.ExerciseDao
import com.marsof.bertra.data.dao.MeasurementUnitDao
import com.marsof.bertra.data.dao.TrainExerciseDao
import com.marsof.bertra.data.entites.Exercise
import com.marsof.bertra.data.entites.MeasurementUnit
import com.marsof.bertra.data.entites.TrainExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TrainExerciseFormUiState(
    val trainExercise: TrainExercise = TrainExercise(
        id = 0,
        trainId = 0,
        exerciseId = 0,
        measurementUnitId = 0
    ),
    val isEntryValid: Boolean = true
)

class AddTrainExerciseScreenViewModel(
    private val trainExerciseDao: TrainExerciseDao,
    exerciseDao: ExerciseDao,
    measurementUnitDao: MeasurementUnitDao
) : ViewModel() {
    var trainExerciseUiState by mutableStateOf(TrainExerciseFormUiState())
    private val _setList = MutableStateFlow<List<Int>>(emptyList())
    val setList: StateFlow<List<Int>> = _setList.asStateFlow()

    // Преобразуем Flow<List<Exercise>> в StateFlow<List<Exercise>>
    // Это рекомендуется для предоставления состояния UI
    val allExercises: StateFlow<List<Exercise>> = exerciseDao.getAllExercises()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Начать сбор, когда UI подписан, с задержкой 5с
            initialValue = emptyList()
        )

    val allMeasurementUnits: StateFlow<List<MeasurementUnit>> =
        measurementUnitDao.getAllMeasurementUnits()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000), // Начать сбор, когда UI подписан, с задержкой 5с
                initialValue = emptyList()
            )

    fun updateUiState(trainExercise: TrainExercise) {
        trainExerciseUiState = TrainExerciseFormUiState(
            trainExercise = trainExercise,
            isEntryValid = validateInput(trainExercise)
        )
    }

    suspend fun saveTrainExercise() {
        if (validateInput()) {
            trainExerciseDao.insert(trainExerciseUiState.trainExercise)
        }
    }

    private fun validateInput(
        trainExercise: TrainExercise = trainExerciseUiState.trainExercise
    ): Boolean {
        return true
        //todo: выбран вес. выбрано упражнение. есть хотя бы один подход.
//        return trainExercise.name.isNotBlank()
    }

    fun addSet() {
        viewModelScope.launch {
            val currentList = _setList.value.toMutableList()
            val newElement = if (currentList.isEmpty()) 1 else currentList.last() + 1
            currentList.add(newElement)
            _setList.value = currentList
        }
    }

    fun clearSetList() {
        viewModelScope.launch {
            _setList.value = emptyList()
        }
    }

    fun updateSetWeight(setIndex: Int, weightOrWeightNumber: Int) {
        val currentList = _setList.value.toMutableList()

        if (setIndex < 0 || setIndex >= currentList.size) {
            val errorMessage = "Index $setIndex is out of bounds for list size ${currentList.size}"
            throw IndexOutOfBoundsException(errorMessage)
        }
        viewModelScope.launch {
            currentList[setIndex] = weightOrWeightNumber
            _setList.value = currentList
        }
    }
}