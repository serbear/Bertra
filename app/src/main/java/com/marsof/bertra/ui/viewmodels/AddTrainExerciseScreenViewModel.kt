package com.marsof.bertra.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import com.marsof.bertra.data.dao.ExerciseDao
import com.marsof.bertra.data.dao.MeasurementUnitDao
import com.marsof.bertra.data.dao.TrainExerciseDao
import com.marsof.bertra.data.dao.TrainExerciseRepetitionsDao
import com.marsof.bertra.data.entites.Exercise
import com.marsof.bertra.data.entites.MeasurementUnit
import com.marsof.bertra.data.entites.TrainExercise
import com.marsof.bertra.data.entites.TrainExerciseRepetitions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

data class TrainExerciseFormUiState(
    val trainExercise: TrainExercise = TrainExercise(
        id = 0,
        trainId = 0,
        exerciseId = 0,
        measurementUnitId = 0
    ),
    val isEntryValid: Boolean = true
)

data class TrainExerciseRepetitionsUiState(
    val repetition: TrainExerciseRepetitions = TrainExerciseRepetitions(
        trainExerciseId = -1,
        setNumber = -1,
        weightOrNumber = -1,
        repetitionsNumber = -1,
        date = null
    )
)


class AddTrainExerciseScreenViewModel(
    private val trainExerciseDao: TrainExerciseDao,
    exerciseDao: ExerciseDao,
    measurementUnitDao: MeasurementUnitDao,
    private val trainExerciseRepetitionsDao: TrainExerciseRepetitionsDao
) : ViewModel() {

    var trainExerciseUiState by mutableStateOf(TrainExerciseFormUiState())
    private val _setList = MutableStateFlow<List<List<Int>>>(emptyList())

    /**
     * The list of the exercise's sets with their weights and repetition number.
     */
    val setList: StateFlow<List<List<Int>>> = _setList.asStateFlow()


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
            //
            // exercise info
            //
            val newExerciseId = trainExerciseDao.insert(trainExerciseUiState.trainExercise)
            //
            // set info
            //
            _setList.value.forEachIndexed { index, set ->
                val idx = index + 1
                val newRepetition = TrainExerciseRepetitions(
                    trainExerciseId = newExerciseId,
                    setNumber = idx,
                    weightOrNumber = set[0],
                    repetitionsNumber = set[1],
                    date = null
                )
                trainExerciseRepetitionsDao.insert(newRepetition)
            }
            // Clear the list.
            _setList.value = emptyList()
        }
    }

    private fun validateInput(
        trainExercise: TrainExercise = trainExerciseUiState.trainExercise
    ): Boolean {
        return true
        //todo: выбран вес. выбрано упражнение. есть хотя бы один подход.
//        return trainExercise.name.isNotBlank()
    }

    fun addSet(weightOrWeightNumber: Int, repetitions: Int) {
        viewModelScope.launch {
            val currentList = _setList.value.toMutableList()
            val newElement = listOf(weightOrWeightNumber, repetitions)
            currentList.add(newElement)
            _setList.value = currentList
        }
    }

    fun clearSetList() {
        viewModelScope.launch {
            _setList.value = emptyList()
        }
    }

    /**
     * Updates the information about the set at the given index in the list of the exercise's set.
     *
     * @param setIndex The index of the set to be updated.
     * @param setData A list containing the new weight (or weight number) and repetitions for the
     * set. The first element is the weight/weight number, and the second is the repetitions.
     * @throws IndexOutOfBoundsException if `setIndex` is out of bounds for the current list of
     * sets.
     */
    fun updateSetWeight(setIndex: Int, setData: List<Int>) {
        val currentList = _setList.value.toMutableList()

        if (setIndex < 0 || setIndex >= currentList.size) {
            val errorMessage = "Index $setIndex is out of bounds for list size ${currentList.size}"
            throw IndexOutOfBoundsException(errorMessage)
        }
        viewModelScope.launch {
            currentList[setIndex] = setData
            _setList.value = currentList
        }
    }
}