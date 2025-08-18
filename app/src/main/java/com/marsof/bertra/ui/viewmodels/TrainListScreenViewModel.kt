package com.marsof.bertra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.data.Train
import com.marsof.bertra.data.TrainDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class TrainListUiState(val taskList: List<Train> = listOf())

class TrainListScreenViewModel(trainDao: TrainDao) : ViewModel() {
    val trainListUiState: StateFlow<TrainListUiState> = trainDao.getAllTrains()
        .map { TrainListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = TrainListUiState()
        )
}
