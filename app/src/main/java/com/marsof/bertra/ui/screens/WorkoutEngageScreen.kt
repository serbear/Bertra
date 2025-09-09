package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.viewmodels.WorkoutEngageScreenViewModel

object WorkoutEngageScreenDestination : INavigationDestination {
    private const val ROUTE_NAME = "workout_engage"
    override val route: String get() = "$ROUTE_NAME/{workoutId}"
    override val titleRes: Int get() = R.string.workout_engage_screen_title

    fun createRoute(workoutId: Long): String {
        return "$ROUTE_NAME/$workoutId"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutEngageScreen(
    navigateToActiveWorkoutScreen: (workoutId: Long) -> Unit,
    viewModel: WorkoutEngageScreenViewModel = viewModel(
        factory = ViewModelProvider.AppViewModelProvider
    ),
    openDrawer: () -> Unit,
    workoutId: Long
) {
    LaunchedEffect(workoutId) {
        viewModel.setWorkoutId(workoutId)
    }

    val workoutTrainListState by viewModel.workoutExercisesByIdState.collectAsState()
    val workoutState by viewModel.workoutState.collectAsState()

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(WorkoutEngageScreenDestination.titleRes),
                onNavigationClick = openDrawer
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            if (workoutState == null) {
                Text(text = "There is no workout with this ID: $workoutId")
            } else {
                //
                // Workout Name
                //
                Text(
                    text = "Workout Name: ${workoutState!!.name}"
                )
                //
                // Workout Description
                //
                Text(
                    text = "Workout Description: ${workoutState!!.description}"
                )
                //
                // todo: Workout Exercises
                //
                if (workoutTrainListState.isEmpty()) {
                    Text(
                        text = "There is no any exercises in the workout.",
                    )
                } else {
                    Text(
                        text = "Workout Exercises:"
                    )
                    LazyColumn {
                        items(
                            items = workoutTrainListState,
                            key = { it.trainExercise.id }) { currentExercise ->
                            Text(
                                text = currentExercise.trainExercise.exerciseId.toString()
                            )
                            Text(
                                text = "Exercise Name: ${currentExercise.exerciseName}"
                            )
                        }
                    }
                }
                //
                // Engage Button
                //
                Button(
                    onClick = { navigateToActiveWorkoutScreen(workoutId) },
                    modifier = Modifier,
                    shape = RoundedCornerShape(0.dp),
                    enabled = workoutTrainListState.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Default.Rocket,
                        contentDescription = stringResource(R.string.workout_engage_button_label),
                    )
                    Text(
                        text = stringResource(R.string.workout_engage_button_label),
                    )
                }
            }
        }
    }
}