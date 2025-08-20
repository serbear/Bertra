package com.marsof.bertra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.data.Train
import com.marsof.bertra.data.TrainDao
import com.marsof.bertra.ui.viewmodels.activetrainstrategies.CurrentTrainStrategy
import com.marsof.bertra.ui.viewmodels.activetrainstrategies.NoActiveTrainStrategy
import com.marsof.bertra.ui.viewmodels.activetrainstrategies.NoTrainsStrategy
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class TrainListUiState(val trainList: List<Train> = listOf())
data class LastTrainUiState(val train: Train?)
class TrainListScreenViewModel(trainDao: TrainDao) : ViewModel() {
    val trainListUiState: StateFlow<TrainListUiState> = trainDao.getAllTrains()
        .map { TrainListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = TrainListUiState()
        )
    val lastTrainUiState: StateFlow<LastTrainUiState> = trainDao.getLastTrain()
        .map { LastTrainUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LastTrainUiState(null)
        )

    val activeTrainStrategy: StateFlow<IActiveTrainStrategy> =
        combine(trainListUiState, lastTrainUiState) { trainListState, lastTrainState ->
            if (trainListState.trainList.isEmpty()) {
                NoTrainsStrategy()
            } else if (lastTrainState.train != null) {
                CurrentTrainStrategy()
            } else {
                NoActiveTrainStrategy()
            }
        }.stateIn( // Use stateIn to make it a StateFlow and manage its lifecycle
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Or Lazily, Eagerly
            initialValue = determineInitialStrategy() // Provide a sensible initial strategy
        )

    private fun determineInitialStrategy(): IActiveTrainStrategy {
        // Logic to determine a sensible default, maybe based on initial values
        // of trainListUiState and lastTrainUiState. This avoids a potential
        // brief moment where the strategy is undefined or uses a placeholder.
        // For example:
        if (trainListUiState.value.trainList.isEmpty()) return NoTrainsStrategy()
        if (lastTrainUiState.value.train != null) return CurrentTrainStrategy()
        return NoActiveTrainStrategy()
    }

}
