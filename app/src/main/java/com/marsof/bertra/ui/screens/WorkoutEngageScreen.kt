package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.elements.BertraErrorMessage
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.theme.LocalCustomColors
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
                onNavigationClick = openDrawer,
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(dimensionResource(R.dimen.button_height)),
            ) {
                Button(
                    onClick = { navigateToActiveWorkoutScreen(workoutId) },
                    enabled = workoutTrainListState.isNotEmpty(),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonColors(
                        containerColor = LocalCustomColors.current.blueButton,
                        contentColor = LocalCustomColors.current.textTertiary,
                        disabledContainerColor = Color.Magenta,
                        disabledContentColor = Color.Magenta,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.button_height))
                        .padding(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Rocket,
                        contentDescription = stringResource(R.string.workout_engage_button_label),
                    )
                    Text(text = stringResource(R.string.workout_engage_button_label))
                }
            }
        }
    ) { innerPadding ->
        var errorMessage: String

        Surface(
            modifier = Modifier,
            color =  LocalCustomColors.current.tertiary,
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(dimensionResource(R.dimen.padding_large))
                    .fillMaxSize(),
            ) {
                if (workoutState == null) {
                    errorMessage = stringResource(R.string.error_message_workout_not_found)
                    BertraErrorMessage(message = "$errorMessage $workoutId")
                } else {
                    val workoutNameLabel = stringResource(R.string.workout_engage_workout_label)
                    val workoutDescriptionLabel =
                        stringResource(R.string.workout_engage_workout_description)
                    //
                    // Workout name and description
                    //
                    Text(
                        text = workoutNameLabel,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                        color = LocalCustomColors.current.textSecondary,
                    )
                    Text(
                        text = workoutState!!.description,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                        color = LocalCustomColors.current.textPrimary,
                    )
                    Text(
                        text = workoutDescriptionLabel,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                        color = LocalCustomColors.current.textSecondary,
                    )
                    Text(
                        text = workoutState!!.description,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                        color = LocalCustomColors.current.textPrimary,
                    )
                    //
                    // Workout Exercises
                    //
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

                    if (workoutTrainListState.isEmpty()) {
                        errorMessage = stringResource(
                            R.string.error_message_workout_exercises_not_found
                        )
                        BertraErrorMessage(message = "$errorMessage $workoutId")
                    } else {
                        Text(
                            text = stringResource(R.string.workout_engage_workout_exercises_label),
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                            color = LocalCustomColors.current.textSecondary,
                        )
                        LazyColumn {
                            items(
                                items = workoutTrainListState,
                                key = { it.trainExercise.id }) { currentExercise ->

                                val exerciseListItemElements = arrayOf(
                                    "${currentExercise.trainExercise.exerciseId}.",
                                    currentExercise.exerciseName
                                )
                                Text(
                                    text = exerciseListItemElements.joinToString(" "),
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                                    color = LocalCustomColors.current.textPrimary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}