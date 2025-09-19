package com.marsof.bertra.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.data.entites.TrainExerciseRepetitions
import com.marsof.bertra.data.entites.TrainExerciseWithExerciseName
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.viewmodels.ActiveWorkoutScreenViewModel

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

    val workoutState = viewModel.workoutState.collectAsState()
    val workoutExercisesList = viewModel.workoutExercisesList.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val timeLeftHundredths by viewModel.timeLeftHundredths.collectAsState()
    val currentExercise = viewModel.currentExercise.collectAsState()
    val currentTimerModeName = viewModel.currentTimerModeName.collectAsState()
    val currentExerciseRepetitions by viewModel.currentExerciseRepetitions.collectAsState()
    val isExerciseAccomplished by viewModel.isExerciseAccomplished.collectAsState()
    val currentTimerMode by viewModel.currentTimerMode.collectAsState()
    val isTimerPaused by viewModel.isTimerPaused.collectAsState()

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(ActiveWorkoutScreenDestination.titleRes),
                onNavigationClick = openDrawer
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Active workout ID: $workoutId",
                color = Color.Magenta,
            ) // debug
            ExerciseData(
                currentExercise = currentExercise
            )
            Spacer(
                Modifier.size(
                    dimensionResource(R.dimen.active_workout_screen_elements_space).value.dp
                )
            )
            if (!isExerciseAccomplished) {
                TimerControl(
                    currentTimerModeName,
                    currentTimerMode,
                    timeLeft,
                    timeLeftHundredths,
                    isTimerPaused,
                    viewModel::setNextTimerMode,
                    viewModel::getNextTimerModeName,
                    viewModel::pauseTimer,
                    viewModel::resumeTimer,
                )
                Spacer(
                    Modifier.size(
                        dimensionResource(R.dimen.active_workout_screen_elements_space).value.dp
                    )
                )
                RepetitionsControl(
                    currentExerciseRepetitions,
                )
            } else {

                // todo: add a button to go to the next exercise or finish the workout.

                Text(text = "Exercise accomplished!")
            }
        }
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
    timeLeftHundredths: Long,
    isTimerPaused: Boolean,
    onChangeTimerMode: () -> Unit = {},
    onGetNextTimeModeName: () -> Int,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
) {
    Column(
        modifier = Modifier,
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
                text = "$timeLeftHundredths",
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
                dimensionResource(R.dimen.timer_control_value_bottom_space).value.dp
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
    Row {
        //
        // Pause and Resume buttons
        //
        if (isTimerPaused) {
            Button(
                onClick = { onResumeTimer() },
                modifier = Modifier,
                shape = RoundedCornerShape(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Restore,
                    contentDescription = stringResource(R.string.resume_timer_button_label),
                )
                Text(
                    text = stringResource(R.string.resume_timer_button_label),
                )
            }
        } else {
            Button(
                onClick = { onPauseTimer() },
                modifier = Modifier,
                shape = RoundedCornerShape(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = stringResource(R.string.pause_timer_button_label),
                )
                Text(
                    text = stringResource(R.string.pause_timer_button_label),
                )
            }
        }
        //
        // Go to Next Mode
        //
        Button(
            onClick = { onChangeTimerMode() },
            modifier = Modifier,
            shape = RoundedCornerShape(0.dp),
            enabled = currentTimerMode == ActiveWorkoutScreenViewModel.TIMER_MODE_WORK
        ) {
            Text(
                text = stringResource(onGetNextTimeModeName()),
            )
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = stringResource(R.string.timer_mode_name_button_label),
            )
        }
    }
}

@Composable
fun RepetitionsControl(currentExerciseRepetitions: List<TrainExerciseRepetitions>?) {
    Column(
        modifier = Modifier,
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
                        weightOrNumber = repetition.weightOrNumber
                    )
                }
            }
        }
        // todo: Change Weight Value Buttons
    }
}

@Composable
fun RepetitionItem(setNumber: Int, repNumber: Int, weightOrNumber: Int) {
    Column(
        modifier = Modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.End,
    ) {
        //
        // Set number
        //
        Text(
            text = "$setNumber",
            fontSize = dimensionResource(R.dimen.repetition_control_set_number).value.sp,
            textAlign = TextAlign.Right,
            modifier = Modifier.padding(end = 1.dp),
        )
        //
        // Number of repetitions and weight
        //
        Box(
            modifier = Modifier
                .border(BorderStroke(1.dp, Color.Magenta)),
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
                    labelText = "REPS"
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Black,
                )
                //
                // Weight or number of a weight
                //
                ValueBoxWithLabel(
                    value = weightOrNumber,
                    labelText = "WGT"
                )
            }
        }
    }
}


@Composable
fun ValueBoxWithLabel(value: Int, labelText: String) {
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
            )
            Text(
                text = labelText,
                fontSize = dimensionResource(R.dimen.repetition_control_set_number).value.sp,
            )
        }
    }
}