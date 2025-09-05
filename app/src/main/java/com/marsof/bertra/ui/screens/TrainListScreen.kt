package com.marsof.bertra.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.data.entites.Train
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.viewmodels.TrainListScreenViewModel

object TrainListScreenDestination : INavigationDestination {
    override val route: String get() = "train_list"
    override val titleRes: Int get() = R.string.train_list_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainListScreen(
    navigateToNewTrainScreen: () -> Unit,
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
                onNavigationClick = openDrawer
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TrainList(
                trainList = trainListState.trainList,
                modifier = Modifier.padding(innerPadding),
            )
            Button(
                onClick = navigateToNewTrainScreen,
                modifier = Modifier,
                shape = RoundedCornerShape(0.dp)
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
}

/**
 * Represents the list of workouts.
 */
@Composable
fun TrainList(trainList: List<Train>, modifier: Modifier) {


    var selectedTrain by remember { mutableStateOf<Train?>(null) }




    Column(
        modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (trainList.isEmpty()) {
            Text(
                text = stringResource(R.string.train_list_is_empty),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            LazyColumn {
                items(
                    items = trainList,
                    key = { it.id }
                ) { train ->
                    SingleTrain(
                        train = train,
                        isSelected = selectedTrain == train,
                        onTrainClick = { clickedTrain ->
                            selectedTrain =
                                if (selectedTrain == clickedTrain) null else clickedTrain
                        }
                    )
                }
            }
        }
    }
}

/**
 * Represents a single list item of the workout list.
 */
@Composable
fun SingleTrain(
    train: Train,
    isSelected: Boolean = false,
    onTrainClick: (Train) -> Unit = {}
) {


    Card(
        modifier = Modifier
            .padding(
                dimensionResource(id = R.dimen.padding_small)
            )
            .clickable {
                onTrainClick(train)
            }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(0.dp)
            ),
        colors = getWorkoutListItemBackgroundColor(isSelected),
        // elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                //.padding(dimensionResource(id = R.dimen.padding_large))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${train.id}: ${train.name}",
                style = MaterialTheme.typography.titleMedium,
                color = getWorkoutListItemTextColor(isSelected),
            )
            Spacer(
                modifier = Modifier.weight(1f)
            )
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Row(
                    modifier = Modifier,
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier,
                        shape = RoundedCornerShape(0.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                        )
                        Text(
                            text = "Edit",
                        )
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier,
                        shape = RoundedCornerShape(0.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Rocket,
                            contentDescription = "Engage",
                        )
                        Text(
                            text = "Engage",
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getWorkoutListItemTextColor(isCardSelected: Boolean): Color {
    return if (isCardSelected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurface
}

@Composable
private fun getWorkoutListItemBackgroundColor(isCardSelected: Boolean): CardColors {
    return CardDefaults.cardColors(
        containerColor = if (isCardSelected)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        else
            MaterialTheme.colorScheme.background
    )
}
