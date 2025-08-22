package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.data.entites.Exercise
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.viewmodels.NewExerciseScreenViewModel
import kotlinx.coroutines.launch

object NewExerciseScreenDestination : INavigationDestination {
    override val route: String get() = "new_exercise"
    override val titleRes: Int get() = R.string.new_exercise_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewExerciseScreen(
    navigateToExerciseListScreen: () -> Unit,
    viewModel: NewExerciseScreenViewModel = viewModel(
        factory = ViewModelProvider.AppViewModelProvider
    ),
    openDrawer: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val exerciseUiState = viewModel.exerciseFormUiState
    val onSaveClick: () -> Unit = {
        coroutineScope.launch {
            viewModel.saveExercise()
            navigateToExerciseListScreen()
        }
    }

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(TrainListScreenDestination.titleRes),
                onNavigationClick = openDrawer
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            ExerciseInputForm(
                exerciseDetails = exerciseUiState.exercise,
                onValueChange = viewModel::updateUiState,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = onSaveClick,
                enabled = exerciseUiState.isEntryValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_exercise))
            }
        }
    }
}

@Composable
fun ExerciseInputForm(
    exerciseDetails: Exercise,
    onValueChange: (Exercise) -> Unit = {},
    modifier: Modifier
) {
    Column(
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = exerciseDetails.name,
            onValueChange = { onValueChange(exerciseDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.exercise_name_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}