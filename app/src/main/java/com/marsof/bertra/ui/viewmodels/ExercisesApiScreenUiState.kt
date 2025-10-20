package com.marsof.bertra.ui.viewmodels

import com.marsof.bertra.api.Exercise
import com.marsof.bertra.data.Muscle

/**
 * Определяет состояния для ExercisesApiScreen.
 */
sealed interface ExercisesApiScreenUiState {
    /**
     * Состояние успешной загрузки данных.
     * @param muscles Список мышц для отображения.
     */
    data class Success<T>(val data: List<T>) : ExercisesApiScreenUiState

    /**
     * Состояние ошибки.
     * @param message Сообщение об ошибке.
     */
    data class Error(val message: String) : ExercisesApiScreenUiState

    /**
     * Состояние загрузки.
     */
    object Loading : ExercisesApiScreenUiState
}