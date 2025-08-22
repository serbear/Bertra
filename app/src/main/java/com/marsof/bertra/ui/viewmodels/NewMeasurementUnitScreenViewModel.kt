package com.marsof.bertra.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.marsof.bertra.data.dao.MeasurementUnitDao
import com.marsof.bertra.data.entites.MeasurementUnit

data class MeasurementUnitFormUiState(
    val measurementUnit: MeasurementUnit = MeasurementUnit(
        id = 0,
        name = ""
    ),
    val isEntryValid: Boolean = false
)

class NewMeasurementUnitScreenViewModel(private val measurementUnitDao: MeasurementUnitDao) :
    ViewModel() {
    var measurementUnitFormUiState by mutableStateOf(MeasurementUnitFormUiState())

    fun updateUiState(measurementUnit: MeasurementUnit) {
        measurementUnitFormUiState = MeasurementUnitFormUiState(
            measurementUnit = measurementUnit,
            isEntryValid = validateInput(measurementUnit)
        )
    }
    private fun validateInput(
        measurementUnit: MeasurementUnit = measurementUnitFormUiState.measurementUnit
    ): Boolean {
        return measurementUnit.name.isNotBlank()
    }
    suspend fun saveMeasurementUnit() {
        if (validateInput()) {
            measurementUnitDao.insert(measurementUnitFormUiState.measurementUnit)
        }
    }
}