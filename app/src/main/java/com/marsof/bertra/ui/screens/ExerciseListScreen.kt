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
import androidx.compose.material3.Text
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
import com.marsof.bertra.data.entites.Exercise
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.viewmodels.ExerciseListScreenViewModel

object ExerciseListScreenDestination : INavigationDestination {
    override val route: String get() = "exercise_list"
    override val titleRes: Int get() = R.string.exercise_list_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseListScreen(
    navigateToNewExerciseScreen: () -> Unit,
    viewModel: ExerciseListScreenViewModel = viewModel(
        factory = ViewModelProvider.AppViewModelProvider
    ),
    openDrawer: () -> Unit
) {
    val exerciseListState by viewModel.exerciseListUiState.collectAsState()

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(ExerciseListScreenDestination.titleRes),
                onNavigationClick = openDrawer
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExerciseList(
                exerciseList = exerciseListState.exerciseList,
                modifier = Modifier.padding(innerPadding),
            )
            Button(
                onClick = navigateToNewExerciseScreen,
                modifier = Modifier,
                shape = RoundedCornerShape(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.new_exercise_button_label),
                )
                Text(
                    text = stringResource(R.string.new_exercise_button_label),
                )
            }
        }
    }
}

@Composable
fun ExerciseList(exerciseList: List<Exercise>, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (exerciseList.isEmpty()) {
            Text(
                text = stringResource(R.string.exercise_list_is_empty),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            LazyColumn {
                items(items = exerciseList, key = { it.id }) { exercise ->
                    SingleExercise(exercise = exercise)
                }
            }
        }
    }
}

@Composable
fun SingleExercise(exercise: Exercise) {
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
                text = exercise.id.toString(),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}