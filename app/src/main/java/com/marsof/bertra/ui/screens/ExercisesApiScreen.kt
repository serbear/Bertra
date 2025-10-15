package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.data.Muscle
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.theme.LocalCustomColors
import com.marsof.bertra.ui.viewmodels.ExercisesApiScreenUiState
import com.marsof.bertra.ui.viewmodels.ExercisesApiScreenViewModel

object ExercisesApiScreenDestination: INavigationDestination{
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
                Text(text="API info")

                MuscleList(viewModel = viewModel)

            }
        }
    }
}

@Composable
fun MuscleList(viewModel: ExercisesApiScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    when(val state = uiState) {
        is ExercisesApiScreenUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is ExercisesApiScreenUiState.Success -> {
            MuscleItems(muscles = state.muscles)
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
fun MuscleItems(muscles: List<Muscle>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(muscles) { muscle ->
            Text(text = muscle.name)
        }
    }
}