package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
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
                trainId = trainId
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
    trainId: Long
) {
    Column(
        modifier = modifier,
    ) {
        TextField (
            value = "Train id: $trainId",
            onValueChange = { onValueChange(trainExerciseDetails.copy(trainId = trainId.toInt())) },
            modifier= Modifier,
            singleLine = true
        )
//        Text(text = "Exercise id:")
//        Text(text = "Repetitions")
//        Text(text = "Measurement unit")
    }
}