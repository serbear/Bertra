package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.data.entites.MeasurementUnit
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.viewmodels.NewMeasurementUnitScreenViewModel
import kotlinx.coroutines.launch

object NewMeasurementUnitScreenDestination : INavigationDestination {
    override val route: String get() = "new_measurement_unit"
    override val titleRes: Int get() = R.string.new_measurement_unit_screen_title
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMeasurementUnitScreen(
    navigateToMeasurementUnitListScreen: () -> Unit,
    viewModel: NewMeasurementUnitScreenViewModel = viewModel(
        factory = ViewModelProvider.AppViewModelProvider
    ),
    openDrawer: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val measurementUnitUiState = viewModel.measurementUnitFormUiState
    val onSaveClick: () -> Unit = {
        coroutineScope.launch {
            viewModel.saveMeasurementUnit()
            navigateToMeasurementUnitListScreen()
        }
    }

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(NewMeasurementUnitScreenDestination.titleRes),
                onNavigationClick = openDrawer,
            )
        },
    ) { innerPadding ->
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.tertiary,
        ) {
            Column(
                modifier = Modifier.padding(innerPadding),
            ) {
                MeasurementUnitInputForm(
                    measurementUnitDetails = measurementUnitUiState.measurementUnit,
                    onValueChange = viewModel::updateUiState,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = onSaveClick,
                    enabled = measurementUnitUiState.isEntryValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.save_measurement_unit))
                }
            }
        }
    }
}

@Composable
fun MeasurementUnitInputForm(
    measurementUnitDetails: MeasurementUnit,
    onValueChange: (MeasurementUnit) -> Unit = {},
    modifier: Modifier
) {
    Column(
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = measurementUnitDetails.name,
            onValueChange = { onValueChange(measurementUnitDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.measurement_unit_name_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}