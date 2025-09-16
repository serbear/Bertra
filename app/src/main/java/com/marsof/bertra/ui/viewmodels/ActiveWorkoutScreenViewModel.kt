package com.marsof.bertra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.R
import com.marsof.bertra.data.dao.TrainDao
import com.marsof.bertra.data.dao.TrainExerciseDao
import com.marsof.bertra.data.dao.TrainExerciseRepetitionsDao
import com.marsof.bertra.data.entites.Train
import com.marsof.bertra.data.entites.TrainExercise
import com.marsof.bertra.data.entites.TrainExerciseRepetitions
import com.marsof.bertra.data.entites.TrainExerciseWithExerciseName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ActiveWorkoutUiState(
    val workout: Train? = null,
    val workoutExerciseList: List<TrainExercise> = emptyList()
)

class ActiveWorkoutScreenViewModel(
    trainDao: TrainDao,
    trainExerciseDao: TrainExerciseDao,
    trainExerciseRepetitionsDao: TrainExerciseRepetitionsDao,
) : ViewModel() {
    private val _trainId = MutableStateFlow(0L)
    private var _currentTimerMode = MutableStateFlow(TIMER_MODE_READY)

    // Начинается с -1, чтобы после вызова goNextExercise индекс был равен первому упражнению.
    private var _currentExerciseIndex = MutableStateFlow(0)

    //
    // Timer related vars & vals
    //
    private val _timeLeft = MutableStateFlow(0L) // Время в секундах
    val timeLeft: StateFlow<Long> = _timeLeft.asStateFlow()
    private var timerJob: Job? = null // Для управления корутиной таймера
    private var _currentRepetitionIndex = MutableStateFlow(0) // Индекс текущего повторения
    private val _isExerciseAccomplished = MutableStateFlow(false)
    val isExerciseAccomplished: StateFlow<Boolean> = _isExerciseAccomplished.asStateFlow()


    // Определяем константы
    companion object {
        const val TIMER_MODE_READY = 0
        const val TIMER_MODE_REST = 1
        const val TIMER_MODE_WORK = 2

        // todo: app settings
        const val READY_TIMER_DURATION = 5L //10L
        const val WORK_TIMER_DURATION = 6L // 180L
        const val REST_TIMER_DURATION = 3L // 120L
    }

//    init {
//        proceedExerciseSequence()
//    }

    val currentTimerModeName: StateFlow<Int> = // Возвращаем ID строки
        _currentTimerMode.map { mode ->
            when (mode) {
                TIMER_MODE_REST -> R.string.rest_timer_mode_name
                TIMER_MODE_WORK -> R.string.work_timer_mode_name
                else -> R.string.ready_timer_mode_name
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
                _isExerciseAccomplished.value = false // Сбрасываем флаг выполнения
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentExerciseRepetitions: StateFlow<List<TrainExerciseRepetitions>?> =
        currentExercise.flatMapLatest { exercise ->
            if (exercise != null) {
                // Сбрасываем индекс повторения при смене упражнения
                _currentRepetitionIndex.value = 0
                _isExerciseAccomplished.value = false // Сбрасываем флаг выполнения
                trainExerciseRepetitionsDao.getTrainExerciseRepetitions(exercise.trainExercise.id)
            } else {
                flowOf(emptyList()) // Return an empty list if there's no current exercise
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList() // Initial value is an empty list
        )

    fun setWorkoutId(trainId: Long) {
        _trainId.value = trainId
        // Сброс состояния при смене тренировки
        _currentExerciseIndex.value = 0
        _currentRepetitionIndex.value = 0
        _isExerciseAccomplished.value = false

        // Start each workout with the Ready Timer.
        setTimerMode(TIMER_MODE_READY)
        startTimer(READY_TIMER_DURATION){
            proceedExerciseSequence()
        }
    }

    fun goNextExercise() {
        val currentIndex = _currentExerciseIndex.value
        val listSize = workoutExercisesList.value.size
        if (listSize > 0 && currentIndex < listSize - 1) {
            _currentExerciseIndex.value = currentIndex + 1

            _isExerciseAccomplished.value = false // Сбрасываем для нового упражнения
            // Сброс индекса повторения и запуск таймера готовности для нового упражнения
            _currentRepetitionIndex.value = 0
//            setTimerMode(TIMER_MODE_READY)
//            startTimer(READY_TIMER_DURATION)

        } else {
            // Опционально: обработать случай, когда это последнее упражнение
            // Например, перейти в режим "завершено" или зациклить
            // Пока просто не даем индексу выйти за пределы
        }
    }

    fun setTimerMode(mode: Int) {
        // Добавим проверку, чтобы убедиться, что mode - это одно из допустимых значений
        if (mode == TIMER_MODE_REST || mode == TIMER_MODE_WORK || mode == TIMER_MODE_READY) {
            _currentTimerMode.value = mode
        }
        // Можно добавить обработку ошибки или логгирование, если mode некорректен
    }

    private fun proceedExerciseSequence() {
        viewModelScope.launch {
            /*
            Waiting for the first value of the 'currentExerciseRepetitions'
            which is not null or not empty.
             */
            val repetitions = currentExerciseRepetitions.first { !it.isNullOrEmpty() }

            val currentRepetitionIndex = _currentRepetitionIndex.value

            /*
            Is the exercise accomplished?
             */
            // "repetitions!!" is safe due to the predicate 'first()'.
            if (currentRepetitionIndex >= repetitions!!.size) {
                _isExerciseAccomplished.value = true
                // todo: Логика завершения упражнения
                return@launch
            }

            _isExerciseAccomplished.value = false

            // Можно использовать, если нужно конкретное повторение
            // val repetition = repetitions[currentRepIndex]

            setTimerMode(TIMER_MODE_WORK)
            startTimer(WORK_TIMER_DURATION) {
                // Логика после завершения таймера работы

                val nextRepetitionIndex = _currentRepetitionIndex.value
                val latestRepetitions = currentExerciseRepetitions.value
                /*
                 Are there more repetitions in the current exercise?
                 */
                if (latestRepetitions != null && nextRepetitionIndex < latestRepetitions.size - 1) {
                    setTimerMode(TIMER_MODE_REST)
                    startTimer(REST_TIMER_DURATION) {
                        _currentRepetitionIndex.value = nextRepetitionIndex + 1
                        // Рекурсивный вызов для следующего повторения.
                        proceedExerciseSequence()
                    }
                } else {
                    // Все подходы текущего упражнения выполнены.
                    _isExerciseAccomplished.value = true
                    // goNextExercise() или другая логика
                }
            }
        }
    }

    fun startTimer(durationSeconds: Long, onFinished: (() -> Unit)? = null) {
        timerJob?.cancel() // Отменяем предыдущий таймер, если он был запущен
        _timeLeft.value = durationSeconds
        timerJob = viewModelScope.launch {
            for (i in durationSeconds downTo 0) {
                _timeLeft.value = i
                delay(1000) // Ждем 1 секунду
            }
            onFinished?.invoke() // Вызываем колбэк по завершении таймера
        }
    }
}
