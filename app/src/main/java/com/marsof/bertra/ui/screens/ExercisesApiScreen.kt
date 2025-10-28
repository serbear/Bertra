package com.marsof.bertra.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.api.Exercise
import com.marsof.bertra.data.Muscle
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.elements.BertraErrorMessage
import com.marsof.bertra.ui.elements.BertraProgressIndicator
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
    val uiState by viewModel.uiStateExercises.collectAsState()
    var muscleSelected by remember { mutableStateOf(false) }
    var isMuscleListShow by remember { mutableStateOf(false) }
    var selectedMuscleName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(ExercisesApiScreenDestination.titleRes),
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
                    onClick = {
                        isMuscleListShow = true//!isMuscleListShow
                        muscleSelected = false
                    },
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
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.muscle_list),
                    )

                    if (muscleSelected) {
                        Text(
                            text = "${stringResource(R.string.muscle_list)} (${selectedMuscleName})"
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.muscle_list),
                        )
                    }
                }
            }
        }
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
                                // Text("Error: Invalid data for exercises")
                            }
                        }

                        is ExercisesApiScreenUiState.Loading -> {
                            BertraProgressIndicator(message = state.message)
                        }

                        is ExercisesApiScreenUiState.Error -> {
                            BertraErrorMessage(message = state.message)
                        }
                    }
                } else if (!isMuscleListShow) {
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
                            text = "List of additional exercises",
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.padding_large))
                        )
                        Text(
                            text = stringResource(R.string.api_description),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.weight(1.618f))
                    }
                } else {
                    MuscleSelector(
                        viewModel = viewModel,
                        onMuscleSelected = { muscle ->
                            viewModel.getExercisesFor(muscle)
                            muscleSelected = true
                            selectedMuscleName = muscle.name
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun MuscleSelector(
    viewModel: ExercisesApiScreenViewModel,
    onMuscleSelected: (Muscle) -> Unit
) {
    Column {
        // Show when the muscle list is displayed.
        MuscleList(
            viewModel = viewModel,
            onMuscleSelected = { muscle ->
                onMuscleSelected(muscle)
            }
        )
    }
}

@Composable
fun MuscleList(
    viewModel: ExercisesApiScreenViewModel,
    onMuscleSelected: (Muscle) -> Unit
) {
    val uiStateMuscles by viewModel.uiStateMuscles.collectAsState()

    when (val state = uiStateMuscles) {
        is ExercisesApiScreenUiState.Loading -> {
            BertraProgressIndicator(message = state.message)
        }

        is ExercisesApiScreenUiState.Success<*> -> {
            MuscleItems(
                muscles = state.data as List<Muscle>,
                onMuscleSelected = onMuscleSelected,
            )
        }

        is ExercisesApiScreenUiState.Error -> {
            BertraErrorMessage(message = state.message)
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
fun ExerciseList(exercises: List<Exercise>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_small)),
        // Space between cards.
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        items(exercises) { exercise ->
            ExerciseInfo(exercise = exercise)
        }
    }
}

@Composable
fun ExerciseInfo(exercise: Exercise) {

    // The state for tracking whether the card is expanded.
    var expanded by remember { mutableStateOf(false) }

    // Animation for the arrow icon to rotate smoothly.
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(LocalCustomColors.current.tertiary)
            .padding(dimensionResource(R.dimen.padding_small))
    )
    {
        // The card header.
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
        ) {
            // Exercise name.
            Text(
                text = exercise.name,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                color = LocalCustomColors.current.textPrimary,
                modifier = Modifier.weight(1f),
            )
            // Difficulty.
            Box(
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = LocalCustomColors.current.blueButton)
                    .padding(
                        start = dimensionResource(R.dimen.padding_small),
                        end = dimensionResource(R.dimen.padding_small),
                    ),
            ) {
                Text(
                    text = exercise.difficulty,
                    color = LocalCustomColors.current.textTertiary,
                )
            }
            // Arrow icon.
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(R.string.expand_exercise_details),
                modifier = Modifier.rotate(rotationAngle),
            )
        }

        Spacer(Modifier.height(dimensionResource(R.dimen.padding_large)))

        AnimatedVisibility(visible = expanded) {
            Column {
                // Text(text = "TYPE: ${exercise.type}")
                // Text(text = "MUSCLE: ${exercise.muscle}")
                Text(
                    text = "EQUIPMENT:",
                    fontWeight = FontWeight.Bold,
                    color = LocalCustomColors.current.textSecondary,
                )
                Text(
                    text = exercise.equipment,
                    color = LocalCustomColors.current.textPrimary,
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
                Text(
                    text = "INSTRUCTIONS:",
                    fontWeight = FontWeight.Bold,
                    color = LocalCustomColors.current.textSecondary,
                )
                Text(
                    text = exercise.instructions,
                    color = LocalCustomColors.current.textPrimary,
                )
            }

        }
        HorizontalDivider()
    }
}
