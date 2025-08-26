package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
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
    override val route: String get() = "add_train_exercise/{trainId}"
    override val titleRes: Int get() = R.string.new_train_exercise_screen_title

    fun createRoute(trainId: Long): String {
        return "add_train_exercise/$trainId"
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
                exercises
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
//        Text(text = "Repetitions")
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
                contentDescription = "Exercise List"
            )
            Text("Exercise List")
        }
        DropdownMenu(
            expanded = menuIsExpanded,
            onDismissRequest = { menuIsExpanded = false }
        ) {
            exercises.forEach { exercise ->
                DropdownMenuItem(
                    text = { Text(exercise.name) },
                    onClick = {

                        onExerciseSelected(exercise.id.toLong())

                        // Закрываем меню после выбора
                        menuIsExpanded = false
                    }
                )
            }
        }
    }
}