package com.marsof.bertra.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.data.entites.TrainExerciseRepetitions
import com.marsof.bertra.data.entites.TrainExerciseWithExerciseName
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.elements.TimerControlButton
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.theme.LocalCustomColors
import com.marsof.bertra.ui.viewmodels.ActiveWorkoutScreenViewModel
import com.marsof.bertra.ui.viewmodels.ActiveWorkoutScreenViewModel.Companion.TIMER_MODE_READY
import com.marsof.bertra.ui.viewmodels.ActiveWorkoutScreenViewModel.Companion.TIMER_MODE_REST
import com.marsof.bertra.ui.viewmodels.ActiveWorkoutScreenViewModel.Companion.TIMER_MODE_WORK

object ActiveWorkoutScreenDestination : INavigationDestination {
    private const val ROUTE_NAME = "active_workout"
    override val route: String get() = "$ROUTE_NAME/{workoutId}"
    override val titleRes: Int get() = R.string.active_workout_screen_title
    fun createRoute(workoutId: Long): String {
        return "${ROUTE_NAME}/$workoutId"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(
    viewModel: ActiveWorkoutScreenViewModel = viewModel(
        factory = ViewModelProvider.AppViewModelProvider
    ),
    openDrawer: () -> Unit,
    workoutId: Long
) {
    LaunchedEffect(workoutId) {
        viewModel.setWorkoutId(workoutId)
    }

    val timeLeft by viewModel.timeLeft.collectAsState()
    val timeLeftTenths by viewModel.timeLeftTenths.collectAsState()
    val currentExercise = viewModel.currentExercise.collectAsState()
    val currentTimerModeName = viewModel.currentTimerModeName.collectAsState()
    val currentExerciseRepetitions by viewModel.currentExerciseRepetitions.collectAsState()
    val isExerciseAccomplished by viewModel.isExerciseAccomplished.collectAsState()
    val isCurrentExerciseLast by viewModel.isCurrentExerciseLast.collectAsState()
    val currentTimerMode by viewModel.currentTimerMode.collectAsState()
    val isTimerPaused by viewModel.isTimerPaused.collectAsState()
    val currentRepetitionIndex by viewModel.currentRepetitionIndex.collectAsState()

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(ActiveWorkoutScreenDestination.titleRes),
                onNavigationClick = openDrawer,
            )
        },
    ) { innerPadding ->
        Surface(
            modifier = Modifier,
            color = LocalCustomColors.current.tertiary,
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (isExerciseAccomplished && isCurrentExerciseLast) {
                        WorkoutComplete()
                    } else {
                        ExerciseInProgressContent(
                            currentExercise,
                            isExerciseAccomplished,
                            currentTimerModeName,
                            currentTimerMode,
                            timeLeft,
                            timeLeftTenths,
                            isTimerPaused,
                            currentExerciseRepetitions,
                            currentRepetitionIndex,
                            viewModel::setNextTimerMode,
                            viewModel::getNextTimerModeName,
                            viewModel::pauseTimer,
                            viewModel::resumeTimer,
                            viewModel::goNextExercise,
                        )
                    }
                }
                Spacer(Modifier.weight(1.618f))
            }
        }
    }
}

@Composable
fun ExerciseInProgressContent(
    currentExercise: State<TrainExerciseWithExerciseName?>,
    isExerciseAccomplished: Boolean,
    currentTimerModeName: State<Int>,
    currentTimerMode: Int,
    timeLeft: Long,
    timeLeftTenths: Long,
    isTimerPaused: Boolean,
    currentExerciseRepetitions: List<TrainExerciseRepetitions>?,
    currentRepetitionIndex: Int,
    onSetNextTimerMode: () -> Unit,
    onGetNextTimerModeName: () -> Int,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
    onGoNextExercise: () -> Unit,
) {
    if (isExerciseAccomplished) {
        ExerciseCompleteControls(onGoNextExercise)
    } else {
        ExerciseData(
            currentExercise = currentExercise,
        )
        Spacer(Modifier.size(dimensionResource(R.dimen.active_workout_screen_elements_space).value.dp))
        TimerControlAndRepetitions(
            currentTimerModeName,
            currentTimerMode,
            timeLeft,
            timeLeftTenths,
            isTimerPaused,
            currentExerciseRepetitions,
            currentRepetitionIndex,
            onSetNextTimerMode,
            onGetNextTimerModeName,
            onPauseTimer,
            onResumeTimer,
        )
    }
}

@Composable
fun TimerControlAndRepetitions(
    currentTimerModeName: State<Int>,
    currentTimerMode: Int,
    timeLeft: Long,
    timeLeftTenths: Long,
    isTimerPaused: Boolean,
    currentExerciseRepetitions: List<TrainExerciseRepetitions>?,
    currentRepetitionIndex: Int,
    onSetNextTimerMode: () -> Unit,
    onGetNextTimerModeName: () -> Int,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
) {
    TimerControl(
        currentTimerModeName,
        currentTimerMode,
        timeLeft,
        timeLeftTenths,
        isTimerPaused,
        onSetNextTimerMode,
        onGetNextTimerModeName,
        onPauseTimer,
        onResumeTimer,
    )
    Spacer(
        Modifier.size(
            dimensionResource(R.dimen.active_workout_screen_elements_space).value.dp
        )
    )
    RepetitionsControl(
        currentExerciseRepetitions,
        currentRepetitionIndex,
        currentTimerMode,
    )
}

/**
 * todo: Если это последнее упражнение в тренировке кнопка "Следующее упражнение" скрыта.
 *  Появляется надпись "Тренировка закончена".
 *  После выполения последнего упражнения обновляется дата lastDate в таблице с тренировками.
 */
@Composable
fun ExerciseCompleteControls(
    onGoNextExercise: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.bear),
            contentDescription = stringResource(R.string.api_description),
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .padding(dimensionResource(R.dimen.padding_large))
        )
        Text(
            text = stringResource(R.string.exercise_complete_text),
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_large))
        )
        Spacer(Modifier.size(dimensionResource(R.dimen.active_workout_screen_elements_space).value.dp))
        Button(
            onClick = { onGoNextExercise() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonColors(
                containerColor = LocalCustomColors.current.blueButton,
                contentColor = LocalCustomColors.current.textTertiary,
                disabledContainerColor = Color.Magenta,
                disabledContentColor = Color.Magenta,
            ),
        ) {
            Text(
                text = stringResource(R.string.go_next_exercise_button_label),
            )
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = stringResource(R.string.go_next_exercise_button_label),
            )
        }
        Spacer(modifier = Modifier.weight(1.618f))
    }
}

@Composable
fun WorkoutComplete() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.bear),
            contentDescription = stringResource(R.string.api_description),
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .padding(dimensionResource(R.dimen.padding_large))
        )
        Text(
            text = "Workout complete!",
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_large))
        )
        Spacer(modifier = Modifier.weight(1.618f))
    }
}

@Composable
fun ExerciseData(currentExercise: State<TrainExerciseWithExerciseName?>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (currentExercise.value != null) {
            Text(
                text = "EXERCISE NAME",
                fontSize = dimensionResource(R.dimen.repetition_control_set_number).value.sp,
            )
            Text(
                text = currentExercise.value?.exerciseName.toString()
            )
        } else {
            // Text(text = "⚠\uFE0F ERROR: no exercise found. ⚠\uFE0F")
        }
    }
}

@Composable
fun TimerControl(
    currentTimerModeName: State<Int>,
    currentTimerMode: Int,
    timeLeft: Long,
    timeLeftTenths: Long,
    isTimerPaused: Boolean,
    onChangeTimerMode: () -> Unit = {},
    onGetNextTimeModeName: () -> Int,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row {
            Text(
                text = "$timeLeft",
                fontSize = dimensionResource(R.dimen.timer_value_text_size).value.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.alignBy(FirstBaseline),
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(
                text = String.format(
                    Locale.current.platformLocale,
                    "%01d",
                    timeLeftTenths,
                ),
                fontSize = dimensionResource(R.dimen.timer_millsec_value_text_size).value.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.alignBy(FirstBaseline),
            )
        }
        //
        // Current timer mode name
        //
        Text(
            text = stringResource(currentTimerModeName.value)
        )
        Spacer(
            Modifier.size(
                dimensionResource(R.dimen.active_workout_screen_elements_space).value.dp
            )
        )
        //
        // Pause and Resume Buttons
        //
        TimerControlButtons(
            currentTimerMode = currentTimerMode,
            isTimerPaused = isTimerPaused,
            onPauseTimer = onPauseTimer,
            onResumeTimer = onResumeTimer,
            onChangeTimerMode = onChangeTimerMode,
            onGetNextTimeModeName = onGetNextTimeModeName,
        )
    }
}

@Composable
fun TimerControlButtons(
    currentTimerMode: Int,
    isTimerPaused: Boolean,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
    onChangeTimerMode: () -> Unit,
    onGetNextTimeModeName: () -> Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        //
        // Pause and Resume buttons
        //
        if (isTimerPaused) {
            TimerControlButton(
                onClickMethod = onResumeTimer,
                text = stringResource(R.string.resume_timer_button_label),
                icon = Icons.Default.Restore,
                modifier = Modifier.weight(1f),
            )
        } else {
            TimerControlButton(
                onClickMethod = onPauseTimer,
                text = stringResource(R.string.pause_timer_button_label),
                icon = Icons.Default.Pause,
                modifier = Modifier.weight(1f),
            )
        }
        //
        // Go to Next Mode
        //
        TimerControlButton(
            onClickMethod = onChangeTimerMode,
            text = stringResource(onGetNextTimeModeName()),
            icon = Icons.Default.SkipNext,
            contentDescription = stringResource(R.string.timer_mode_name_button_label),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
fun RepetitionsControl(
    currentExerciseRepetitions: List<TrainExerciseRepetitions>?,
    currentRepetitionIndex: Int,
    currentTimerMode: Int,
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "REPETITIONS",
            fontSize = dimensionResource(R.dimen.repetition_control_set_number).value.sp,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (currentExerciseRepetitions.isNullOrEmpty()) {
                Text(text = "ERROR: There is no data on the exercise repetitions.")
            } else {
                currentExerciseRepetitions.forEach { repetition ->
                    RepetitionItem(
                        setNumber = repetition.setNumber,
                        repNumber = repetition.repetitionsNumber,
                        weightOrNumber = repetition.weightOrNumber,
                        currentRepetitionIndex = currentRepetitionIndex,
                        currentTimerMode = currentTimerMode,
                    )
                }
            }
        }
        // todo: Change Weight Value Buttons
    }
}

@Composable
fun RepetitionItem(
    setNumber: Int,
    repNumber: Int,
    weightOrNumber: Int,
    currentRepetitionIndex: Int,
    currentTimerMode: Int,
) {
    val (valueColor, valueNameColor) = getCurrentRepetitionTextColors(
        setNumber,
        currentRepetitionIndex,
        currentTimerMode
    )

    Column(
        modifier = Modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //
        // Set number
        //
        Text(
            text = "$setNumber",
            fontSize = dimensionResource(R.dimen.repetition_control_set_number).value.sp,
            textAlign = TextAlign.Right,
            modifier = Modifier.padding(end = 1.dp),
            color = LocalCustomColors.current.textPrimary,
        )
        //
        // Number of repetitions and weight
        //
        Box(
            modifier = Modifier
                .background(
                    color = getCurrentRepetitionMarker(
                        setNumber,
                        currentRepetitionIndex,
                        currentTimerMode,
                    )
                ),
        ) {
            Column(
                modifier = Modifier.width(IntrinsicSize.Min),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                //
                // Number of repetitions
                //
                ValueBoxWithLabel(
                    value = repNumber,
                    labelText = "REPS",
                    valueTextColor = valueColor,
                    valueNameTextColor = valueNameColor,
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(0.75f),
                    thickness = 1.dp,
                    color = LocalCustomColors.current.secondary,
                )
                //
                // Weight or number of a weight
                //
                ValueBoxWithLabel(
                    value = weightOrNumber,
                    labelText = "WGT",
                    valueTextColor = valueColor,
                    valueNameTextColor = valueNameColor,
                )
            }
        }
    }
}

/**
 * Returns the color for the current repetition displaying [Box] background.
 *
 * Own colors have the following elements:
 *
 * - The current repetition.
 * - The Future repetitions.
 * - The Accomplished repetitions.
 *
 * @param setNumber The current set number from the [TrainExerciseRepetitions] object.
 * @param currentRepetitionIndex The index of the current repetition.
 * In fact, it can be considered as a number of a displaying [Box].
 * @param currentTimerMode The current timer mode.
 * @return The color for the current repetition marker.
 */
@Composable
private fun getCurrentRepetitionMarker(
    setNumber: Int,
    currentRepetitionIndex: Int,
    currentTimerMode: Int
): Color {
    if (setNumber > currentRepetitionIndex + 1) {
        // Future repetition.
        return Color.Transparent
    } else if (setNumber < currentRepetitionIndex + 1) {
        // Accomplished repetition.
        return LocalCustomColors.current.primary
    }
    // Current repetition.
    return when (currentTimerMode) {
        TIMER_MODE_READY -> LocalCustomColors.current.secondary
        TIMER_MODE_WORK -> LocalCustomColors.current.additional
        TIMER_MODE_REST -> LocalCustomColors.current.primary
        else -> Color.Transparent
    }
}

@Composable
fun ValueBoxWithLabel(
    value: Int,
    labelText: String,
    valueTextColor: Color,
    valueNameTextColor: Color
) {
    Box(
        modifier = Modifier
        //.border(BorderStroke(1.dp, Color.Magenta))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.repetition_control_value_padding).value.dp,
                end = dimensionResource(R.dimen.repetition_control_value_padding).value.dp,
                top = dimensionResource(R.dimen.repetition_control_value_padding).value.dp,
                bottom = 0.dp
            ),
        ) {
            Text(
                text = value.toString(),
                fontSize = dimensionResource(R.dimen.repetition_control_repetition_number).value.sp,
                color = valueTextColor,
            )
            Text(
                text = labelText,
                fontSize = dimensionResource(R.dimen.repetition_control_set_number).value.sp,
                color = valueNameTextColor,
            )
        }
    }
}

/**
 * Определяет цвета для текста значения и его подписи в зависимости от состояния подхода.
 *
 * @param setNumber Номер текущего подхода.
 * @param currentRepetitionIndex Индекс текущего выполняемого подхода.
 * @param currentTimerMode Текущий режим таймера (готовность, работа, отдых).
 * @return Pair<Color, Color>, где first - цвет для значения (числа),
 * а second - цвет для подписи (REPS/WGT).
 */
@Composable
private fun getCurrentRepetitionTextColors(
    setNumber: Int,
    currentRepetitionIndex: Int,
    currentTimerMode: Int
): Pair<Color, Color> {
    val textSecondary = LocalCustomColors.current.textSecondary

    // Логика для будущих и выполненных подходов одинакова для обоих цветов
    if (setNumber != currentRepetitionIndex + 1) {
        return textSecondary to textSecondary
    }

    // Логика для текущего подхода
    return when (currentTimerMode) {
        TIMER_MODE_READY -> textSecondary to textSecondary
        TIMER_MODE_WORK -> LocalCustomColors.current.primary to LocalCustomColors.current.tertiary
        TIMER_MODE_REST -> textSecondary to textSecondary
        else -> Color.Magenta to Color.Magenta // Для отладки
    }
}