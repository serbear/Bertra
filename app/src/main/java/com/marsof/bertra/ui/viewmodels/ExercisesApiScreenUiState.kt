package com.marsof.bertra.ui.viewmodels

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
     * Loading state.
     * @param message Message displayed while loading.
     */
    data class Loading(val message: String) : ExercisesApiScreenUiState
}