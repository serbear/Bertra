package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
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
            modifier = Modifier.padding(innerPadding),
        ) {
            Text(
                text = "Active workout ID: $workoutId"
            )

            ExerciseData(currentExercise)

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

                // todo: Repetitions

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
        modifier = Modifier
    ) {
        if (currentExercise.value != null) {
            Text(text = "Exercise Name: ${currentExercise.value?.exerciseName}")
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
        modifier = Modifier
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "$timeLeft",
                fontSize = dimensionResource(R.dimen.timer_value_text_size).value.sp
            )
            Text(
                text = "."
            )
            Text(
                text = "$timeLeftHundredths",
                fontSize = dimensionResource(R.dimen.timer_millsec_value_text_size).value.sp
            )
        }
        Text(
            text = stringResource(currentTimerModeName.value)
        )
        //
        // Pause and Resume Buttons
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
                contentDescription = stringResource(
                    R.string.timer_mode_name_button_label
                ),
            )
        }
    }
}

@Composable
fun RepetitionsControl(currentExerciseRepetitions: List<TrainExerciseRepetitions>?) {
    Column(
        modifier = Modifier
    ) {
        Text(text = "Repetitions")
        Text(text = "Change Weight Value Buttons")
    }
}
