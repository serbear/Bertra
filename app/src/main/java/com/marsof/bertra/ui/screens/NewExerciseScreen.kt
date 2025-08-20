package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.viewmodels.NewExerciseScreenViewModel

object NewExerciseScreenDestination : INavigationDestination {
    override val route: String get() = "new_exercise"
    override val titleRes: Int get() = R.string.new_exercise_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewExerciseScreen(
    navigateToScreen: () -> Unit,
    viewModel: NewExerciseScreenViewModel = viewModel(factory = ViewModelProvider.AppViewModelProvider),
    openDrawer: () -> Unit
) {
    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(TrainListScreenDestination.titleRes),
                onNavigationClick = openDrawer
            )
        },
    ) { innerPadding ->
        Text(
            text = "New Exercise",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(innerPadding)
        )
    }
}