package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.marsof.bertra.R
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination

object ActiveWorkoutScreenDestination : INavigationDestination {
    private const val ROUTE_NAME = "active_workout"
    override val route: String get() = "$ROUTE_NAME/{workoutId}"
    override val titleRes: Int get() = R.string.active_workout_screen_title
    fun createRoute(workoutId: Long): String {
        return "${ROUTE_NAME}/$workoutId"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(
    // viewModel: ActiveWorkoutScreenViewModel = viewModel(factory = ViewModelProvider.AppViewModelProvider)
    openDrawer: () -> Unit,
    workoutId: Long
) {
//    LaunchedEffect(workoutId) {
//        viewModel.setWorkoutId(workoutId)
//    }

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(ActiveWorkoutScreenDestination.titleRes),
                onNavigationClick = openDrawer
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            Text(text = "Active workout ID: $workoutId")
        }
    }
}
