package com.marsof.bertra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.viewmodels.HomeScreenViewModel
import com.marsof.bertra.ui.viewmodels.IActiveTrainStrategy
import com.marsof.bertra.ui.viewmodels.activetrainstrategies.CurrentTrainStrategy

object HomeScreenDestination : INavigationDestination {
    override val route: String get() = "home"
    override val titleRes: Int get() = R.string.home_screen_title
}

/**
 * The screen which is showed after the loading process of the application
 * or after activating the "Home Screen" menu item through the application main menu.
 *
 * The screen displays various components depending on information in the database.
 * There are three cases:
 *
 * - There is no any data on trainings in the database.
 *   The screen displays the message and the "Add New Train" button.
 * - There is one or more records on trainings in the database, but no information about the last
 *   training. The screen displays the corresponding message and the "Choose Train" button.
 * - There is one or more records on trainings in the database, and information about the last
 *   training exists too. The screen displays the last train information
 *   and the "Continue Train" button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToTaskListScreen: () -> Unit,
    viewModel: HomeScreenViewModel = viewModel(factory = ViewModelProvider.AppViewModelProvider),
    openDrawer: () -> Unit
) {
//    val currentLastTrainStrategyState by viewModel.currentLastTrainStrategyState.collectAsState()

    val currentDisplayStrategy = viewModel.getActiveTrainStrategy()

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(HomeScreenDestination.titleRes),
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
            LastTrainComponent(
                currentDisplayStrategy,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

/**
 * Displays a message text and an action button whose text and action are depending on
 * the Home Screen display strategy [currentStrategy].
 */
@Composable
fun LastTrainComponent(
    currentStrategy: IActiveTrainStrategy,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TrainStatusText(currentStrategy, Modifier)
        TrainButton(currentStrategy, Modifier)
    }
}

/**
 * Displays a text depending on the Home Screen display strategy [currentStrategy].
 *
 * The text for the element is provided by [currentStrategy].
 */
@Composable
fun TrainStatusText(currentStrategy: IActiveTrainStrategy, modifier: Modifier) {
    val trainStatusText = if (currentStrategy is CurrentTrainStrategy) {
        currentStrategy.getDisplayText()
    } else {
        stringResource(currentStrategy.getDisplayTextFromResource())
    }

    Text(
        text = trainStatusText,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
    )
}

/**
 * Represents the action button whose label and click action depending on the Home Screen
 * display strategy.
 */
@Composable
fun TrainButton(currentStrategy: IActiveTrainStrategy, modifier: Modifier) {
    Button(
        onClick = { currentStrategy.executeAction() },
        modifier = modifier,
        shape = RoundedCornerShape(0.dp)
    ) {
        Text(
            text = stringResource(currentStrategy.getButtonLabelText()),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

