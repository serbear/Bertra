package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.data.entites.Train
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.viewmodels.NewTrainScreenViewModel
import kotlinx.coroutines.launch

object NewTrainScreenDestination : INavigationDestination {
    override val route: String get() = "new_train"
    override val titleRes: Int get() = R.string.new_train_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTrainScreen(
    navigateToTrainExercisesScreen: () -> Unit,
    viewModel: NewTrainScreenViewModel = viewModel(factory = ViewModelProvider.AppViewModelProvider),
    openDrawer: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val trainFormUiState = viewModel.trainFormUiState
    val onSaveClick: () -> Unit = {
        coroutineScope.launch {
            viewModel.saveTrain()
            navigateToTrainExercisesScreen()
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
//            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
        ) {
            TrainInputForm(
                trainDetails = trainFormUiState.train,
                onValueChange = viewModel::updateUiState,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = onSaveClick,
                enabled = trainFormUiState.isEntryValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.go_train_exercise))
            }
        }
    }
}

@Composable
fun TrainInputForm(
    trainDetails: Train,
    onValueChange: (Train) -> Unit = {},
    modifier: Modifier
) {

    Column(
        modifier = modifier,
//        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = trainDetails.name,
            onValueChange = { onValueChange(trainDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.train_name_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        NumberStepper(
            value = trainDetails.circles,
            onValueChange = { onValueChange(trainDetails.copy(circles = it)) },
//            modifier = Modifier.padding(vertical = 16.dp),
            minValue = 1,
            maxValue = 100,
            step = 1
        )


        OutlinedTextField(
            value = trainDetails.description,
            onValueChange = { onValueChange(trainDetails.copy(description = it)) },
            label = { Text(stringResource(R.string.train_description)) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    min = 100.dp
                ),
            singleLine = false,
            maxLines = 5
        )
    }
}

@Composable
fun NumberStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minValue: Int = Int.MIN_VALUE,
    maxValue: Int = Int.MAX_VALUE,
    step: Int = 1
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onValueChange((value - step).coerceAtLeast(minValue)) },
            enabled = value > minValue
        ) {
            Icon(Icons.Default.Remove, "Decrease")
        }
        Text(
            text = "$value",
//            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        IconButton(
            onClick = { onValueChange((value + step).coerceAtMost(maxValue)) },
            enabled = value < maxValue
        ) {
            Icon(Icons.Default.Add, "Increase")
        }
    }
}