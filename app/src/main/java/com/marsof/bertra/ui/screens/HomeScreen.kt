package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.viewmodels.HomeScreenViewModel
import com.marsof.bertra.ui.viewmodels.IActiveTrainStrategy
import com.marsof.bertra.ui.viewmodels.activetrainstrategies.CurrentTrainStrategy

object HomeScreenDestination : INavigationDestination {
    override val route: String get() = "home"
    override val titleRes: Int get() = R.string.home_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToTaskForm: () -> Unit,
    viewModel: HomeScreenViewModel = viewModel(factory = ViewModelProvider.Factory),
    openDrawer: () -> Unit
) {
//    val currentLastTrainStrategyState by viewModel.currentLastTrainStrategyState.collectAsState()

    val currentStrategy = viewModel.getLastTrainStrategy()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(HomeScreenDestination.titleRes))
                },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Открыть меню")
                    }
                }
            )
        },
    ) { innerPadding ->
        LastTrainComponent(
            currentStrategy,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Composable
fun LastTrainComponent(
    currentStrategy: IActiveTrainStrategy,
    modifier: Modifier
) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TrainStatusText(currentStrategy, Modifier)
        TrainButton(currentStrategy, Modifier)
    }

}

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

