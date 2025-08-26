package com.marsof.bertra.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.marsof.bertra.data.entites.Train
import com.marsof.bertra.data.dao.TrainDao

data class TrainFormUiState(
    val train: Train = Train(
        id = 0,
        name = "",
        circles = 1,
        description = "",
        lastDate = null
    ),
    val isEntryValid: Boolean = false
)

class NewTrainScreenViewModel(private val trainDao: TrainDao) : ViewModel() {
    var trainFormUiState by mutableStateOf(TrainFormUiState())

    fun updateUiState(train: Train) {
        trainFormUiState = TrainFormUiState(
            train = train,
            isEntryValid = validateInput(train)
        )
    }

    suspend fun saveTrain(): Long {
        if (validateInput()) {
            return trainDao.insert(trainFormUiState.train)
        }
        return -1
    }

    private fun validateInput(task: Train = trainFormUiState.train): Boolean {
        return task.name.isNotBlank()
    }
}