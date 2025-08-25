package com.marsof.bertra.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.marsof.bertra.data.dao.TrainExerciseDao
import com.marsof.bertra.data.entites.TrainExercise

data class TrainExerciseFormUiState(
    val trainExercise: TrainExercise = TrainExercise(
        id = 0,
        trainId = 0,
        exerciseId = 0,
        repetitionsNumber = 1,
        measurementUnitId = 0
    ),
    val isEntryValid: Boolean = false
)

class AddTrainExerciseScreenViewModel(
    private val trainExerciseDao: TrainExerciseDao
) : ViewModel() {
    var trainExerciseUiState by mutableStateOf(TrainExerciseFormUiState())

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
}