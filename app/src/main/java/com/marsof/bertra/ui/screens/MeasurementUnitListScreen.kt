package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.data.entites.MeasurementUnit
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.viewmodels.MeasurementUnitListScreenViewModel

object MeasurementUnitListScreenDestination : INavigationDestination {
    override val route: String get() = "measurement_unit_list"
    override val titleRes: Int get() = R.string.measurement_unit_list_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasurementUnitListScreen(
    openDrawer: () -> Unit,
    navigateToNewMeasurementUnitScreen: () -> Unit,
    viewModel: MeasurementUnitListScreenViewModel = viewModel(
        factory = ViewModelProvider.AppViewModelProvider
    )
) {
    val measurementUnitListState by viewModel.measurementUnitListUiState.collectAsState()

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(MeasurementUnitListScreenDestination.titleRes),
                onNavigationClick = openDrawer,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
    ) { innerPadding ->
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.tertiary,
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MeasurementUnitList(
                    measurementUnitList = measurementUnitListState.measurementUnitList,
                    modifier = Modifier.padding(innerPadding),
                )
                Button(
                    onClick = navigateToNewMeasurementUnitScreen,
                    modifier = Modifier,
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.new_measurement_unit_button_label),
                    )
                    Text(
                        text = stringResource(R.string.new_measurement_unit_button_label),
                    )
                }
            }
        }
    }
}

@Composable
fun MeasurementUnitList(
    measurementUnitList: List<MeasurementUnit>,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (measurementUnitList.isEmpty()) {
            Text(
                text = stringResource(R.string.measurement_unit_list_is_empty),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            LazyColumn {
                items(items = measurementUnitList, key = { it.id }) { measurementUnit ->
                    SingleMeasurementUnit(measurementUnit = measurementUnit)
                }
            }
        }
    }
}

@Composable
fun SingleMeasurementUnit(measurementUnit: MeasurementUnit) {
    Card(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_large))
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_small)
            )
        ) {
            Text(
                text = measurementUnit.id.toString(),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = measurementUnit.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}