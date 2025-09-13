package com.marsof.bertra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.R
import com.marsof.bertra.data.dao.TrainDao
import com.marsof.bertra.data.dao.TrainExerciseDao
import com.marsof.bertra.data.entites.Train
import com.marsof.bertra.data.entites.TrainExercise
import com.marsof.bertra.data.entites.TrainExerciseWithExerciseName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ActiveWorkoutUiState(
    val workout: Train? = null,
    val workoutExerciseList: List<TrainExercise> = emptyList()
)

class ActiveWorkoutScreenViewModel(
    trainDao: TrainDao,
    trainExerciseDao: TrainExerciseDao
) : ViewModel() {
    private val _trainId = MutableStateFlow(0L)
    private var _currentTimerMode = MutableStateFlow(TIMER_MODE_WORK)
    private var _currentExerciseIndex =
        MutableStateFlow(0)// Начинаем с первого упражнения (индекс 0)

    // Определяем константы для режимов таймера для лучшей читаемости
    companion object {
        const val TIMER_MODE_REST = 0
        const val TIMER_MODE_WORK = 1
    }

    val currentTimerModeName: StateFlow<Int> = // Возвращаем ID строки
        _currentTimerMode.map { mode ->
            if (mode == TIMER_MODE_REST) {
                R.string.rest_timer_mode_name
            } else {
                R.string.work_timer_mode_name
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = R.string.work_timer_mode_name
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val workoutState: StateFlow<Train?> =
        _trainId.flatMapLatest { trainId ->
            if (trainId > 0L) {
                trainDao.getTrain(trainId)
            } else {
                flowOf(null)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val workoutExercisesList: StateFlow<List<TrainExerciseWithExerciseName>> =
        _trainId.flatMapLatest { trainId ->
            if (trainId > 0L) {
                // При смене ID тренировки, сбрасываем индекс упражнения на 0
                _currentExerciseIndex.value = 0
                trainExerciseDao.getTrainExercisesById(trainId)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    // Комбинируем _currentExerciseIndex и workoutExercisesList
    val currentExercise: StateFlow<TrainExerciseWithExerciseName?> =
        combine(_currentExerciseIndex, workoutExercisesList) { index, list ->
            // list.getOrNull(index) безопасно вернет элемент по индексу или null,
            // если индекс выходит за пределы списка.
            list.getOrNull(index)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = null // Начальное значение null, пока список не загружен или индекс не действителен
        )


    fun setWorkoutId(trainId: Long) {
        _trainId.value = trainId
        // При установке нового ID тренировки, также сбрасываем индекс упражнения
        // _currentExerciseIndex.value = 0 // Уже делается в flatMapLatest для workoutExercisesList
    }

    fun goNextExercise() {
        val currentIndex = _currentExerciseIndex.value
        val listSize = workoutExercisesList.value.size
        if (listSize > 0 && currentIndex < listSize - 1) {
            _currentExerciseIndex.value = currentIndex + 1
        } else {
            // Опционально: обработать случай, когда это последнее упражнение
            // Например, перейти в режим "завершено" или зациклить
            // Пока просто не даем индексу выйти за пределы
        }
    }
    fun goPreviousExercise() {
        val currentIndex = _currentExerciseIndex.value
        if (currentIndex > 0) {
            _currentExerciseIndex.value = currentIndex - 1
        }
    }
    fun setTimerMode(mode: Int) {
        // Добавим проверку, чтобы убедиться, что mode - это одно из допустимых значений
        if (mode == TIMER_MODE_REST || mode == TIMER_MODE_WORK) {
            _currentTimerMode.value = mode
        }
        // Можно добавить обработку ошибки или логгирование, если mode некорректен
    }
}