package com.marsof.bertra.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.data.dao.ExerciseDao
import com.marsof.bertra.data.dao.TrainExerciseDao
import com.marsof.bertra.data.entites.Exercise
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
        repetitionsNumber = 1,
        measurementUnitId = 0
    ),
    val isEntryValid: Boolean = true
)

class AddTrainExerciseScreenViewModel (
    private val trainExerciseDao: TrainExerciseDao,
    exerciseDao: ExerciseDao,
) : ViewModel() {
    var trainExerciseUiState by mutableStateOf(TrainExerciseFormUiState())
    private val _repetitionList = MutableStateFlow<List<Int>>(emptyList())
    /**
     * The list of an exercise repetition. Their order and weight or weight number.
     */
    val repetitionList: StateFlow<List<Int>> = _repetitionList.asStateFlow()

    // Преобразуем Flow<List<Exercise>> в StateFlow<List<Exercise>>
    // Это рекомендуется для предоставления состояния UI
    val allExercises: StateFlow<List<Exercise>> = exerciseDao.getAllExercises()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Начать сбор, когда UI подписан, с задержкой 5с
            initialValue = emptyList() // Начальное значение, пока данные не загрузятся
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

    fun addRepetition() {
        viewModelScope.launch {
            val currentList = _repetitionList.value.toMutableList()
            val newElement = if (currentList.isEmpty()) 1 else currentList.last() + 1
            currentList.add(newElement)
            _repetitionList.value = currentList
        }
    }

    fun clearRepetitionsList() {
        viewModelScope.launch {
            _repetitionList.value = emptyList()
        }
    }

    fun updateRepetitionValue(index: Int, value : Int){
        val currentList = _repetitionList.value.toMutableList()

        if (index < 0 || index >= currentList.size) {
            throw IndexOutOfBoundsException(
                "Index $index is out of bounds for list size ${currentList.size}"
            )
        }

        viewModelScope.launch {
            currentList[index] = value
            _repetitionList.value = currentList
        }
    }
}