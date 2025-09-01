package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.data.entites.Exercise
import com.marsof.bertra.data.entites.MeasurementUnit
import com.marsof.bertra.data.entites.TrainExercise
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.viewmodels.AddTrainExerciseScreenViewModel
import kotlinx.coroutines.launch

object AddTrainExerciseScreenDestination : INavigationDestination {
    const val ROUTE_NAME = "add_train_exercise"
    override val route: String get() = "$ROUTE_NAME/{trainId}"
    override val titleRes: Int get() = R.string.new_train_exercise_screen_title

    fun createRoute(trainId: Long): String {
        return "$ROUTE_NAME/$trainId"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrainExerciseScreen(
    navigateToScreen: () -> Unit,
    viewModel: AddTrainExerciseScreenViewModel = viewModel(
        factory = ViewModelProvider.AppViewModelProvider
    ),
    openDrawer: () -> Unit,
    trainId: Long,
) {
    val coroutineScope = rememberCoroutineScope()

    val trainExerciseUiState = viewModel.trainExerciseUiState

    val trainExerciseSetList by viewModel.setList.collectAsStateWithLifecycle()


    val onSaveClick: () -> Unit = {
        coroutineScope.launch {
            viewModel.saveTrainExercise()
            navigateToScreen()
        }
    }

    // Собираем StateFlow и преобразуем его в State
    // collectAsStateWithLifecycle рекомендуется для безопасного сбора Flow в Compose UI
    val exercises by viewModel.allExercises.collectAsStateWithLifecycle()
    val measurementUnits by viewModel.allMeasurementUnits.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(AddTrainExerciseScreenDestination.titleRes),
                onNavigationClick = openDrawer
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            TrainExerciseInputForm(
                trainExerciseDetails = trainExerciseUiState.trainExercise,
                exerciseSetDetails = trainExerciseSetList,
                onValueChange = viewModel::updateUiState,
                trainId = trainId,
                exercises = exercises,
                measurementUnits = measurementUnits,
//                setList = viewModel.setList.collectAsState().value,
                onSetAdd = viewModel::addSet,
                onSetWeightChange = viewModel::updateSetWeight,
                modifier = Modifier.fillMaxWidth(),
            )
            Row {
                Button(
                    onClick = navigateToScreen,
                    enabled = true,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.go_back_button_label))
                }
                Button(
                    onClick = onSaveClick,
                    enabled = trainExerciseUiState.isEntryValid,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.save_train_exercise))
                }
            }
        }
    }
}

@Composable
fun TrainExerciseInputForm(
    trainExerciseDetails: TrainExercise,
    onValueChange: (TrainExercise) -> Unit = {},
    modifier: Modifier,
    trainId: Long,
    exercises: List<Exercise>,
    measurementUnits: List<MeasurementUnit>,
//    setList: List<List<Int>>,
    onSetWeightChange: (Int, List<Int>) -> Unit,
    exerciseSetDetails: List<List<Int>>,
    onSetAdd: (Int, Int) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        TextField(
            value = "Train id: $trainId",
            onValueChange = { onValueChange(trainExerciseDetails.copy(trainId = trainId)) },
            modifier = Modifier,
            singleLine = true
        )
        Text(text = "Exercise id:")
        ExercisesDropdownMenu(
            exercises = exercises,
            onExerciseSelected = { selectedExerciseId ->
                onValueChange(trainExerciseDetails.copy(exerciseId = selectedExerciseId))
            }
        )
        SetList(exerciseSetDetails, onSetAdd, onSetWeightChange)
        MeasurementUnitDropdownMenu(
            measurementUnits = measurementUnits,
            onMeasureUnitSelected = { selectedMeasurementUnitId ->
                onValueChange(
                    trainExerciseDetails.copy(
                        measurementUnitId = selectedMeasurementUnitId
                    )
                )
            }
        )
    }
}

@Composable
fun ExercisesDropdownMenu(
    exercises: List<Exercise>,
    onExerciseSelected: (Long) -> Unit,
) {
    var menuIsExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Button(
            onClick = { menuIsExpanded = !menuIsExpanded },
            modifier = Modifier,
            shape = RoundedCornerShape(0.dp)
        ) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.exercise_list_button_label)
            )
            Text(stringResource(R.string.exercise_list_button_label))
        }
        DropdownMenu(
            expanded = menuIsExpanded,
            onDismissRequest = { menuIsExpanded = false }
        ) {
            exercises.forEach { exercise ->
                DropdownMenuItem(
                    text = { Text(exercise.name) },
                    onClick = {
                        onExerciseSelected(exercise.id)
                        menuIsExpanded = false // Close menu after the choosing.
                    }
                )
            }
        }
    }
}

@Composable
fun SetList(
    exerciseSetDetails: List<List<Int>>,
    onSetAdd: (Int, Int) -> Unit,
    onSetWeightChange: (Int, List<Int>) -> Unit,
) {
    Column(
        modifier = Modifier,
    ) {

        //
        // The list of repetitions
        //
        if (exerciseSetDetails.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
//                    .weight(1f),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(exerciseSetDetails) { index, setData ->
                    SetListItem(
                        index = index,
                        setData,
                        onSetWeightChange
                    )
                }
            }
        } else {
            Text(
                text = "List is empty. Press the button to add a new one.",
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.weight(1f),
            )
        }

        //
        // The button for adding a new set.
        //
        Button(
            onClick = {
                // weight, repetitions
                // Default values: 0, 1
                onSetAdd(0, 1)
            },
            modifier = Modifier,
            shape = RoundedCornerShape(0.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = stringResource(R.string.add_repetition_button_label)
            )
            Text(stringResource(R.string.add_repetition_button_label))
        }
    }
}

@Composable
fun SetListItem(
    index: Int,
    setData: List<Int>,
    onSetWeightChange: (Int, List<Int>) -> Unit
) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "# $index")
            //
            // Weight or Weight Number
            //
            Text(text = "Weight:")
            NumberStepper(
                value = setData[0],
                onValueChange = { newWeightValue ->
                    onSetWeightChange(
                        index,
                        listOf(newWeightValue, setData[1])
                    )
                },
                minValue = 1,
                maxValue = 999,
                step = 1
            )
            //
            // Set repetition number.
            //
            Text(text = "Reps:")
            NumberStepper(
                value = setData[1],
                onValueChange = { newRepetitionsValue ->
                    onSetWeightChange(
                        index,
                        listOf(setData[0], newRepetitionsValue)
                    )
                },
                minValue = 1,
                maxValue = 999,
                step = 1
            )
            //
            // Set order buttons
            //
            Button(
                onClick = { },
                modifier = Modifier,
                shape = RoundedCornerShape(0.dp)
            ) {
                Icon(
                    Icons.Default.ArrowDropUp,
                    contentDescription = stringResource(R.string.move_repetition_up_button_label)
                )
            }
            Button(
                onClick = { },
                modifier = Modifier,
                shape = RoundedCornerShape(0.dp)
            ) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.move_repetition_down_button_label)
                )
            }
        }
    }
}

@Composable
fun MeasurementUnitDropdownMenu(
    measurementUnits: List<MeasurementUnit>,
    onMeasureUnitSelected: (Long) -> Unit
) {
    var menuIsExpanded by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf<MeasurementUnit?>(null) }

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Button(
            onClick = { menuIsExpanded = !menuIsExpanded },
            modifier = Modifier,
            shape = RoundedCornerShape(0.dp)
        ) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.measurement_unit_list_button_label)
            )
            Text(stringResource(R.string.measurement_unit_list_button_label))
        }
        DropdownMenu(
            expanded = menuIsExpanded,
            onDismissRequest = { menuIsExpanded = false }
        ) {
            measurementUnits.forEach { measurementUnit ->
                DropdownMenuItem(
                    text = { Text(measurementUnit.name) },
                    onClick = {
                        selectedUnit = measurementUnit // Сохраняем выбранную единицу
                        onMeasureUnitSelected(measurementUnit.id)
                        menuIsExpanded = false // Close menu after the choosing.
                    }
                )
            }
        }
    }
    Text(text = selectedUnit?.name ?: stringResource(R.string.measurement_unit_is_not_selected))
}
