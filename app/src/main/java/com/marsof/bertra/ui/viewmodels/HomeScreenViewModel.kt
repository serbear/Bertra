package com.marsof.bertra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.ui.viewmodels.activetrainstrategies.CurrentTrainStrategy
import com.marsof.bertra.ui.viewmodels.activetrainstrategies.NoActiveTrainStrategy
import com.marsof.bertra.ui.viewmodels.activetrainstrategies.NoTrainsStrategy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//data class IsLastTrainExistsUiState(val isLastTrainExists: Boolean)

data class CurrentLastTrainStrategy(val currentLastTrainStrategy: NoTrainsStrategy)

class HomeScreenViewModel() : ViewModel() {
//    private val _uiState = MutableStateFlow(IsLastTrainExistsUiState(false))
//    val uiState: StateFlow<IsLastTrainExistsUiState> = _uiState.asStateFlow()

//    private val _currentLastTrainStrategyState =
//        MutableStateFlow(CurrentLastTrainStrategy(NoTrainsStrategy()))
//    val currentLastTrainStrategyState: StateFlow<CurrentLastTrainStrategy> =
//        _currentLastTrainStrategyState.asStateFlow()



    // Функция для изменения состояния
//    fun setIsLastTrainExists(value: Boolean) {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isLastTrainExists = value)
//        }
//    }

    fun getLastTrainStrategy(): IActiveTrainStrategy {
        val trainExists =false
        val lastTrainExists = true

        // todo: проверить наличие тренировок в базе.

        if (!trainExists) return NoTrainsStrategy()

        // todo: проверить наличие последней тренировки.

        if (lastTrainExists) return CurrentTrainStrategy()

        return NoActiveTrainStrategy()
    }
}