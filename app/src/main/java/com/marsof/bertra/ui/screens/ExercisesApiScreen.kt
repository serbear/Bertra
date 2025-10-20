package com.marsof.bertra.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.api.Exercise
import com.marsof.bertra.data.Muscle
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.theme.LocalCustomColors
import com.marsof.bertra.ui.viewmodels.ExercisesApiScreenUiState
import com.marsof.bertra.ui.viewmodels.ExercisesApiScreenViewModel

object ExercisesApiScreenDestination : INavigationDestination {
    private const val ROUTE_NAME = "exercises_api"
    override val route: String get() = ROUTE_NAME
    override val titleRes: Int get() = R.string.exercises_api_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesApiScreen(
    viewModel: ExercisesApiScreenViewModel = viewModel(
        factory = ViewModelProvider.AppViewModelProvider
    ),
    openDrawer: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var muscleSelected by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(ExercisesApiScreenDestination.titleRes),
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
            ) {

                if (muscleSelected) {
                    when (val state = uiState) {
                        is ExercisesApiScreenUiState.Success<*> -> {
                            val exercises = state.data as? List<Exercise>
                            if (exercises != null) {
                                ExerciseList(exercises = exercises)
                            } else {
                                // Обработка случая, когда данные не являются списком упражнений
//                                Text("Error: Invalid data for exercises")
                            }
                        }
                        is ExercisesApiScreenUiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                                Text(text = state.message)
                            }
                        }
                        is ExercisesApiScreenUiState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Error: ${state.message}")
                            }
                        }
                    }
                } else {
                    Text(text = "API info")
                    MuscleSelector(
                        viewModel = viewModel,
                        onMuscleSelected = { muscle ->
                            viewModel.getExercisesFor(muscle)
                            muscleSelected = true
                        }
                    )
                }




            }
        }
    }
}

@Composable
fun MuscleSelector(
    viewModel: ExercisesApiScreenViewModel,
    onMuscleSelected: (Muscle) -> Unit,
) {
    var isListShow by remember { mutableStateOf(false) }

    Column {
        if (isListShow) {
            // Show when the muscle list is displayed.
            MuscleList(
                viewModel = viewModel,
                onMuscleSelected = { muscle ->
                    onMuscleSelected(muscle)
                }
            )
        } else {
            // Show when the muscle list is hidden.
            Text(text = "Select a muscle:")
            Button(
                onClick = {
                    // Toggle the list displaying.
                    isListShow = !isListShow
                },
                modifier = Modifier,
                shape = RoundedCornerShape(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.muscle_list),
                )
                Text(
                    text = stringResource(R.string.muscle_list),
                )
            }

        }
    }
}

@Composable
fun MuscleList(
    viewModel: ExercisesApiScreenViewModel,
    onMuscleSelected: (Muscle) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ExercisesApiScreenUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
                Text(text = state.message)
            }
        }

        is ExercisesApiScreenUiState.Success<*> -> {
            MuscleItems(
                muscles = state.data as List<Muscle>,
                onMuscleSelected = onMuscleSelected,
            )
        }

        is ExercisesApiScreenUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error: ${state.message}")
            }
        }
    }
}

@Composable
fun MuscleItems(
    muscles: List<Muscle>,
    modifier: Modifier = Modifier,
    onMuscleSelected: (Muscle) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(muscles) { muscle ->
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.button_height))
                    .clickable {
                        onMuscleSelected(muscle)
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = muscle.name,
                )
            }
        }
    }
}

@Composable
fun ExerciseInfo(exercise: Exercise) {
    Column {
        Text(text = "NAME: ${exercise.name}")
        Text(text = "TYPE: ${exercise.type}")
        Text(text = "MUSCLE: ${exercise.muscle}")
        Text(text = "EQUIPMENT: ${exercise.equipment}")
        Text(text = "DIFFICULTY: ${exercise.difficulty}")
        Text(text = "INSTRUCTIONS: ${exercise.instructions}")
    }
}
@Composable
fun ExerciseList(exercises: List<Exercise>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(exercises) { exercise ->
            ExerciseInfo(exercise = exercise)
        }
    }
}