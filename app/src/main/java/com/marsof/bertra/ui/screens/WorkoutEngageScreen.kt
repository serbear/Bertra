package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    val workoutTrainListState by viewModel.workoutExercisesByIdState.collectAsState()

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(TrainListScreenDestination.titleRes),
                onNavigationClick = openDrawer
            )
        },
    ) { innerPadding ->
        Text(
            text = "Engage Screen for the Workout $workoutId",
            modifier = Modifier.padding(innerPadding),
        )
    }
}