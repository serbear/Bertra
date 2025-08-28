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
import androidx.compose.runtime.collectAsState
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
    val onSaveClick: () -> Unit = {
        coroutineScope.launch {
            viewModel.saveTrainExercise()
            navigateToScreen()
        }
    }

    // Собираем StateFlow и преобразуем его в State
    // collectAsStateWithLifecycle рекомендуется для безопасного сбора Flow в Compose UI
    val exercises by viewModel.allExercises.collectAsStateWithLifecycle()

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
                onValueChange = viewModel::updateUiState,
                modifier = Modifier.fillMaxWidth(),
                trainId = trainId,
                exercises = exercises,
                repetitionList = viewModel.repetitionList.collectAsState().value,
                onRepetitionAdd = viewModel::addRepetition,
                onRepetitionValueChange = viewModel::updateRepetitionValue,
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
    repetitionList: List<Int>,
    onRepetitionAdd: () -> Unit,
    onRepetitionValueChange: (Int, Int) -> Unit

) {
    Column(
        modifier = modifier,
    ) {
        TextField(
            value = "Train id: $trainId",
            onValueChange = { onValueChange(trainExerciseDetails.copy(trainId = trainId.toInt())) },
            modifier = Modifier,
            singleLine = true
        )
        Text(text = "Exercise id:")
        MinimalDropdownMenu(
            exercises = exercises,
            onExerciseSelected = { selectedExerciseId ->
                onValueChange(trainExerciseDetails.copy(exerciseId = selectedExerciseId.toInt()))
            }
        )
        RepetitionsList(repetitionList, onRepetitionAdd, onRepetitionValueChange)
//        Text(text = "Measurement unit")
    }
}

@Composable
fun MinimalDropdownMenu(
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
                        onExerciseSelected(exercise.id.toLong())  // toLong remove after db schema change.
                        menuIsExpanded = false // Close menu after the choosing.
                    }
                )
            }
        }
    }
}

@Composable
fun RepetitionsList(
    repetitionList: List<Int>,
    onRepetitionAdd: () -> Unit,
    onRepetitionValueChange: (Int, Int) -> Unit
) {
    Column(
        modifier = Modifier,
    ) {

        //
        // The list of repetitions
        //
        if (repetitionList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
//                    .weight(1f),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(repetitionList) { index, value ->
                    RepetitionListItem(index, repetitionList, onRepetitionValueChange)
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
        // The button for adding a new repetition.
        //
        Button(
            onClick = { onRepetitionAdd() },
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
fun RepetitionListItem(
    index: Int,
    repetitionList: List<Int>,
    onRepetitionValueChange: (Int, Int) -> Unit
) {
    val value = repetitionList[index]

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
            Text(
                text = "$index: $value",
            )
            //
            // Number stepper
            //
            NumberStepper(
                value = value,
                onValueChange = { newValue ->
                    onRepetitionValueChange(index, newValue)
                },
                minValue = 1,
                maxValue = 999,
                step = 1
            )
            //
            // Repetition order buttons
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