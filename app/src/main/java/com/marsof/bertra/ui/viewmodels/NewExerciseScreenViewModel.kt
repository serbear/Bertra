package com.marsof.bertra.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.marsof.bertra.data.dao.ExerciseDao
import com.marsof.bertra.data.entites.Exercise

data class ExerciseFormUiState(
    val exercise: Exercise = Exercise(
        id = 0,
        name = ""
    ),
    val isEntryValid: Boolean = false
)

class NewExerciseScreenViewModel(private val exerciseDao: ExerciseDao) : ViewModel() {
    var exerciseFormUiState by mutableStateOf(ExerciseFormUiState())

    fun updateUiState(exercise: Exercise) {
        exerciseFormUiState = ExerciseFormUiState(
            exercise = exercise,
            isEntryValid = validateInput(exercise)
        )
    }

    private fun validateInput(exercise: Exercise = exerciseFormUiState.exercise): Boolean {
        return exercise.name.isNotBlank()
    }

    suspend fun saveExercise() {
        if (validateInput()) {
            exerciseDao.insert(exerciseFormUiState.exercise)
        }
    }

}