package com.marsof.bertra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.data.dao.MeasurementUnitDao
import com.marsof.bertra.data.entites.MeasurementUnit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class MeasurementUnitUiState(val measurementUnitList: List<MeasurementUnit> = listOf())

class MeasurementUnitListScreenViewModel(measurementUnitDao: MeasurementUnitDao) : ViewModel() {
    val measurementUnitListUiState: StateFlow<MeasurementUnitUiState> =
        measurementUnitDao.getAllMeasurementUnits()
            .map { MeasurementUnitUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = MeasurementUnitUiState()
            )
}