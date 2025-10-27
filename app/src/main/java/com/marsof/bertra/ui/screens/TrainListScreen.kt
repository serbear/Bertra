package com.marsof.bertra.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.data.entites.Train
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.theme.LocalCustomColors
import com.marsof.bertra.ui.viewmodels.TrainListScreenViewModel

object TrainListScreenDestination : INavigationDestination {
    override val route: String get() = "train_list"
    override val titleRes: Int get() = R.string.train_list_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainListScreen(
    navigateToNewTrainScreen: () -> Unit,
    navigateToWorkoutEngageScreen: (workoutId: Long) -> Unit,
    viewModel: TrainListScreenViewModel = viewModel(
        factory = ViewModelProvider.AppViewModelProvider
    ),
    openDrawer: () -> Unit,
) {
    val trainListState by viewModel.trainListUiState.collectAsState()

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(TrainListScreenDestination.titleRes),
                onNavigationClick = openDrawer,
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor =Color.Transparent,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(dimensionResource(R.dimen.button_height)),
            ) {
                Button(
                    onClick = navigateToNewTrainScreen,
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonColors(
                        containerColor = LocalCustomColors.current.blueButton,
                        contentColor = LocalCustomColors.current.textTertiary,
                        disabledContainerColor = Color.Magenta,
                        disabledContentColor = Color.Magenta,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.button_height))
                        .padding(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.new_train_button_label),
                    )
                    Text(
                        text = stringResource(R.string.new_train_button_label),
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            color = LocalCustomColors.current.tertiary,
        ) {
            TrainList(
                trainList = trainListState.trainList,
                navigateToWorkoutEngageScreen = navigateToWorkoutEngageScreen,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }
    }
}

/**
 * Represents the list of workouts.
 */
@Composable
fun TrainList(
    trainList: List<Train>,
    modifier: Modifier,
    navigateToWorkoutEngageScreen: (workoutId: Long) -> Unit
) {
    var selectedTrain by remember { mutableStateOf<Train?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (trainList.isEmpty()) {
            Text(
                text = stringResource(R.string.train_list_is_empty),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp),
            )
        } else {
            LazyColumn(
                modifier = modifier.weight(1f)
            ) {
                items(
                    items = trainList,
                    key = { it.id }
                ) { train ->
                    SingleTrain(
                        train = train,
                        isSelected = selectedTrain == train,
                        onTrainClick = { clickedTrain ->
                            selectedTrain = getSelectedTrain(selectedTrain, clickedTrain)
                        },
                        navigateToWorkoutEngageScreen = navigateToWorkoutEngageScreen,
                    )
                }
            }
        }
    }
}

private fun getSelectedTrain(selectedTrain: Train?, clickedTrain: Train): Train? {
    return if (selectedTrain == clickedTrain) null else clickedTrain
}

/**
 * Represents a single list item of the workout list.
 */
@Composable
fun SingleTrain(
    train: Train,
    isSelected: Boolean = false,
    onTrainClick: (Train) -> Unit = {},
    navigateToWorkoutEngageScreen: (workoutId: Long) -> Unit
) {
    Card(
        modifier = Modifier
            .clickable {
                onTrainClick(train)
            }
            .height(dimensionResource(R.dimen.button_height)),
        colors = getWorkoutListItemBackgroundColor(isSelected),
        shape = RoundedCornerShape(0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = dimensionResource(R.dimen.padding_small) * 2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = train.name,
                style = MaterialTheme.typography.titleMedium,
                color = getWorkoutListItemTextColor(isSelected),
            )
            Spacer(
                modifier = Modifier.weight(1f)
            )
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandHorizontally(),
                exit = ExitTransition.None,
            ) {
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    //
                    // Edit workout button
                    //
                    TextButton(
                        onClick = {},
                        modifier = Modifier.fillMaxHeight(),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = LocalCustomColors.current.additional,
                            contentColor = LocalCustomColors.current.primary,
                            disabledContainerColor = Color.Magenta,
                            disabledContentColor = Color.Magenta,
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                        )
                        Text(
                            text = "Edit",
                        )
                    }
                    //
                    // Engage workout button
                    //
                    TextButton(
                        onClick = { navigateToWorkoutEngageScreen(train.id) },
                        modifier = Modifier.fillMaxHeight(),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = LocalCustomColors.current.additional,
                            contentColor = LocalCustomColors.current.primary,
                            disabledContainerColor = Color.Magenta,
                            disabledContentColor = Color.Magenta,
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Rocket,
                            contentDescription =
                                stringResource(R.string.workout_engage_button_label),
                        )
                        Text(
                            text = stringResource(R.string.workout_engage_button_label),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getWorkoutListItemTextColor(isCardSelected: Boolean): Color {
    return if (isCardSelected) LocalCustomColors.current.primary
    else LocalCustomColors.current.textPrimary
}

@Composable
private fun getWorkoutListItemBackgroundColor(isCardSelected: Boolean): CardColors {
    return CardDefaults.cardColors(
        containerColor = if (isCardSelected)
            LocalCustomColors.current.additional
        else
            LocalCustomColors.current.tertiary,
    )
}
