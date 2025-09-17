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
import kotlin.math.ceil

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

    //
    // Timer related vars & vals
    //
    private var _currentTimerMode = MutableStateFlow(TIMER_MODE_READY)
    private val _timeLeft = MutableStateFlow(0L)
    val timeLeft: StateFlow<Long> = _timeLeft.asStateFlow()
    private var targetTimeMillis: Long = 0L
    private var pausedTimeMillis: Long = 0L
    // Timer coroutine control.
    private var timerJob: Job? = null
    var currentTimerMode = _currentTimerMode.asStateFlow()
    private var timerCompletionCallback: (() -> Unit)? = null
    private val _timeLeftHundredths = MutableStateFlow(0L)
    val timeLeftHundredths: StateFlow<Long> = _timeLeftHundredths.asStateFlow()
    //
    // Exercise related vars & vals
    //
    private var _currentRepetitionIndex = MutableStateFlow(0)
    private val _isExerciseAccomplished = MutableStateFlow(false)
    private var _currentExerciseIndex = MutableStateFlow(0)
    val isExerciseAccomplished: StateFlow<Boolean> = _isExerciseAccomplished.asStateFlow()

    //
    // Pause functionality related vars and vals
    //
    private val _isTimerPaused = MutableStateFlow(false)
    val isTimerPaused: StateFlow<Boolean> = _isTimerPaused.asStateFlow()

    //
    // Consts
    //
    companion object {
        const val TIMER_MODE_READY = 0
        const val TIMER_MODE_REST = 1
        const val TIMER_MODE_WORK = 2

        // todo: app settings
        const val READY_TIMER_DURATION = 5L //10L
        const val WORK_TIMER_DURATION = 6L // 180L
        const val REST_TIMER_DURATION = 3L // 120L
        // UI Update frequency.
        private const val TIMER_TICK_INTERVAL_MS = 100L
    }

    val currentTimerModeName: StateFlow<Int> =
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
                _currentExerciseIndex.value = 0
                _isExerciseAccomplished.value = false
                trainExerciseDao.getTrainExercisesById(trainId)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    val currentExercise: StateFlow<TrainExerciseWithExerciseName?> =
        combine(_currentExerciseIndex, workoutExercisesList) { index, list ->
            list.getOrNull(index)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentExerciseRepetitions: StateFlow<List<TrainExerciseRepetitions>?> =
        currentExercise.flatMapLatest { exercise ->
            if (exercise != null) {
                _currentRepetitionIndex.value = 0
                _isExerciseAccomplished.value = false
                trainExerciseRepetitionsDao.getTrainExerciseRepetitions(exercise.trainExercise.id)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    fun setWorkoutId(trainId: Long) {
        _trainId.value = trainId
        _currentExerciseIndex.value = 0
        _currentRepetitionIndex.value = 0
        _isExerciseAccomplished.value = false
        // Start each workout with the Ready Timer.
        setTimerMode(TIMER_MODE_READY)
        startTimer(READY_TIMER_DURATION) {
            proceedExerciseSequence()
        }
    }

    fun goNextExercise() {
        val currentIndex = _currentExerciseIndex.value
        val listSize = workoutExercisesList.value.size
        if (listSize > 0 && currentIndex < listSize - 1) {
            _currentExerciseIndex.value = currentIndex + 1
            // Сбрасываем для нового упражнения
            _isExerciseAccomplished.value = false
            // Сброс индекса повторения и запуск таймера готовности для нового упражнения
            _currentRepetitionIndex.value = 0
        } else {
            // Опционально: обработать случай, когда это последнее упражнение
            // Например, перейти в режим "завершено" или зациклить
            // Пока просто не даем индексу выйти за пределы
        }
    }

    fun setTimerMode(mode: Int) {
        // Let's add a check to ensure that mode is one of the allowed values.
        if (mode == TIMER_MODE_REST || mode == TIMER_MODE_WORK || mode == TIMER_MODE_READY) {
            _currentTimerMode.value = mode
        }
        // Можно добавить обработку ошибки или логгирование, если mode некорректен
    }

    /**
     * Sets the next timer mode based on the current timer mode.
     *
     * This function is responsible for transitioning between timer modes TIMER_MODE_WORK
     * and TIMER_MODE_REST during a workout.
     *
     * - If the current mode is `TIMER_MODE_WORK`:
     *   - It checks if there are more repetitions for the current exercise.
     *     - If yes, it transitions to `TIMER_MODE_REST`, increments the repetition index,
     *       and starts the rest timer. After the rest timer finishes,
     *       it calls `proceedExerciseSequence()` to move to the next work cycle.
     *     - If no (all repetitions for the current exercise are done),
     *       it sets `_isExerciseAccomplished` to true.
     * - No action is taken if the current mode is `TIMER_MODE_REST` or any other unhandled state,
     *   as transitions from these states are either not defined or handled elsewhere.
     */
    fun setNextTimerMode() {
        viewModelScope.launch {
            when (_currentTimerMode.value) {
                TIMER_MODE_WORK -> {
                    // Check if there are more repetitions in the current exercise.
                    val reps = currentExerciseRepetitions.value
                    val currentRepIndex = _currentRepetitionIndex.value
                    if (reps != null && currentRepIndex < reps.size - 1) {
                        setTimerMode(TIMER_MODE_REST)
                        // Move to the next repetition.
                        _currentRepetitionIndex.value = currentRepIndex + 1
                        // Not accomplished yet, moving to rest.
                        // _isExerciseAccomplished.value = false
                        startTimer(REST_TIMER_DURATION) {
                            // After rest, proceed to the next work cycle.
                            proceedExerciseSequence()
                        }
                    } else {
                        // All repetitions for the current exercise are done.
                        _isExerciseAccomplished.value = true

                    }
                }
            }
        }
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

            setTimerMode(TIMER_MODE_WORK)
            startTimer(WORK_TIMER_DURATION) {
                val nextRepetitionIndex = _currentRepetitionIndex.value
                val latestRepetitions = currentExerciseRepetitions.value
                // Are there more repetitions in the current exercise?
                if (latestRepetitions != null && nextRepetitionIndex < latestRepetitions.size - 1) {
                    setTimerMode(TIMER_MODE_REST)
                    startTimer(REST_TIMER_DURATION) {
                        _currentRepetitionIndex.value = nextRepetitionIndex + 1
                        proceedExerciseSequence()
                    }
                } else {
                    // All repetitions are done.
                    _isExerciseAccomplished.value = true
                }
            }
        }
    }

    fun pauseTimer() {
        if (!_isTimerPaused.value && (timerJob?.isActive == true)) {
            timerJob?.cancel()
            // Remaining time.
            pausedTimeMillis = targetTimeMillis - System.currentTimeMillis()
            _isTimerPaused.value = true
        }
    }

    fun resumeTimer() {
        if (_isTimerPaused.value && pausedTimeMillis > 0) {
            _isTimerPaused.value = false
            targetTimeMillis = System.currentTimeMillis() + pausedTimeMillis

            // Перезапускаем корутину таймера с обновленным targetTimeMillis
            // Нам нужно знать, какая функция onFinished была передана изначально.
            // Для этого ее нужно сохранить или перестроить логику.
            // Простой вариант - передавать onFinished в resumeTimer,
            // но это усложнит API.
            // Пока что предполагаем, что onFinished - это setNextTimerMode или proceedExerciseSequence,
            // которые вызываются после соответствующего таймера.
            // Для более чистого решения, возможно, понадобится сохранить лямбду onFinished.

            // ВАЖНО: нужно знать, какой onFinished был у прерванного таймера.
            // Это ограничение текущего подхода с сохранением pausedTimeMillis.
            // Один из вариантов - сохранить лямбду onFinished при старте таймера.
            // Но для примера, давайте предположим, что мы знаем, какой коллбек вызвать.
            // Это место требует доработки в зависимости от логики вашего приложения.

            // Примерный перезапуск (потребует сохранения исходного onFinished):
            val originalDurationSeconds = (_timeLeft.value) // Приблизительное время, можно уточнить
            val originalOnFinished = timerCompletionCallback // Нужно сохранить этот коллбек

            timerJob?.cancel()
            _isTimerPaused.value = false

            timerJob = viewModelScope.launch {
                while (true) {
                    if (_isTimerPaused.value) {
                        delay(TIMER_TICK_INTERVAL_MS)
                        continue
                    }

                    val currentTimeMillis = System.currentTimeMillis()
                    val remainingMillis = targetTimeMillis - currentTimeMillis

                    if (remainingMillis <= 0) {
                        _timeLeft.value = 0
                        _timeLeftHundredths.value = 0
                        break
                    }
                    _timeLeft.value = ceil(remainingMillis.toDouble() / 1000.0).toLong()
                    _timeLeftHundredths.value = remainingMillis / 10L

                    val delayMillis = if (remainingMillis % 1000L == 0L) {
                        minOf(TIMER_TICK_INTERVAL_MS, 1000L)
                    } else {
                        minOf(TIMER_TICK_INTERVAL_MS, remainingMillis % 1000L)
                    }
                    delay(delayMillis)
                }
                originalOnFinished?.invoke()
            }
        }
    }

    fun startTimer(durationSeconds: Long, onFinished: (() -> Unit)? = null) {
        timerJob?.cancel()
        _isTimerPaused.value = false
        this.timerCompletionCallback = onFinished

        val durationMillis = durationSeconds * 1000L
        targetTimeMillis = System.currentTimeMillis() + durationMillis
        _timeLeft.value = durationSeconds
        _timeLeftHundredths.value = durationMillis

        timerJob = viewModelScope.launch {
            while (true) {
                if (_isTimerPaused.value) {
                    delay(TIMER_TICK_INTERVAL_MS)
                    continue
                }

                val currentTimeMillis = System.currentTimeMillis()
                val remainingMillis = targetTimeMillis - currentTimeMillis

                if (remainingMillis <= 0) {
                    _timeLeft.value = 0
                    _timeLeftHundredths.value = 0
                    break
                }

                _timeLeft.value = ceil(remainingMillis.toDouble() / 1000.0).toLong()
                _timeLeftHundredths.value = remainingMillis / 10L

                val delayMillis = if (remainingMillis % 1000L == 0L) {
                    minOf(TIMER_TICK_INTERVAL_MS, 1000L)
                } else {
                    minOf(TIMER_TICK_INTERVAL_MS, remainingMillis % 1000L)
                }
                delay(delayMillis)
            }
            this@ActiveWorkoutScreenViewModel.timerCompletionCallback?.invoke()
        }
    }
    /**
     * Returns the resource ID for the name of the next timer mode.
     *
     * This function determines the name of the next timer mode based on the current timer mode.
     * - If the current mode is `TIMER_MODE_READY` or `TIMER_MODE_REST`,
     *   the next mode will be "Work".
     * - Otherwise (implying the current mode is `TIMER_MODE_WORK`),
     *   the next mode will be "Rest".
     *
     * @return The resource ID (Int) of the string representing the next timer mode's name.
     */
    fun getNextTimerModeName(): Int {
        return when (_currentTimerMode.value) {
            TIMER_MODE_READY, TIMER_MODE_REST -> {
                R.string.work_timer_mode_name
            }

            else -> {
                R.string.rest_timer_mode_name
            }
        }
    }
}
