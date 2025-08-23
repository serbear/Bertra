package com.marsof.bertra.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.marsof.bertra.ui.screens.ExerciseListScreen
import com.marsof.bertra.ui.screens.ExerciseListScreenDestination
import com.marsof.bertra.ui.screens.HomeScreen
import com.marsof.bertra.ui.screens.HomeScreenDestination
import com.marsof.bertra.ui.screens.MeasurementUnitListScreen
import com.marsof.bertra.ui.screens.MeasurementUnitListScreenDestination
import com.marsof.bertra.ui.screens.NewExerciseScreen
import com.marsof.bertra.ui.screens.NewExerciseScreenDestination
import com.marsof.bertra.ui.screens.NewMeasurementUnitScreen
import com.marsof.bertra.ui.screens.NewMeasurementUnitScreenDestination
import com.marsof.bertra.ui.screens.NewTrainScreen
import com.marsof.bertra.ui.screens.NewTrainScreenDestination
import com.marsof.bertra.ui.screens.TrainExercisesListScreen
import com.marsof.bertra.ui.screens.TrainExercisesListScreenDestination
import com.marsof.bertra.ui.screens.TrainListScreen
import com.marsof.bertra.ui.screens.TrainListScreenDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The list of the destination points for the Side Menu.
 */
val sideMenuDestinations = listOf(
    HomeScreenDestination,
    TrainListScreenDestination,
    NewTrainScreenDestination,
    ExerciseListScreenDestination,
    MeasurementUnitListScreenDestination,
)

/**
 * Manages navigation between the application screens.
 *
 * @param navController the navController for this host.
 * @param scope CoroutineScope for running coroutines (used for opening the drawer).
 * @param drawerState the state of the side menu of the application.
 */
@Composable
fun NavigationHost(
    navController: NavHostController,
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    NavHost(
        navController = navController,
        startDestination = HomeScreenDestination.route
    ) {
        composable(route = HomeScreenDestination.route) {
            HomeScreen(
                openDrawer = { scope.launch { drawerState.open() } },
                navigateToTaskListScreen = {
                    navController.navigate(TrainListScreenDestination.route)
                }
            )
        }
        composable(route = NewExerciseScreenDestination.route) {
            NewExerciseScreen(
                openDrawer = { scope.launch { drawerState.open() } },
                navigateToExerciseListScreen = {
                    navController.navigate(ExerciseListScreenDestination.route)
                }
            )
        }
        composable(route = ExerciseListScreenDestination.route) {
            ExerciseListScreen(
                openDrawer = { scope.launch { drawerState.open() } },
                navigateToNewExerciseScreen = {
                    navController.navigate(NewExerciseScreenDestination.route)
                }
            )
        }
        composable(route = TrainExercisesListScreenDestination.route) {
            TrainExercisesListScreen(
                openDrawer = { scope.launch { drawerState.open() } },
                navigateToScreen = {}
            )
        }
        composable(route = NewTrainScreenDestination.route) {
            NewTrainScreen(
                openDrawer = { scope.launch { drawerState.open() } },
                navigateToTrainExercisesListScreen = {
                    navController.navigate(TrainExercisesListScreenDestination.route)
                }
            )
        }
        composable(route = TrainListScreenDestination.route) {
            TrainListScreen(
                openDrawer = { scope.launch { drawerState.open() } },
                navigateToNewTrainScreen = {
                    navController.navigate(NewTrainScreenDestination.route)
                }
            )
        }
        composable(route = NewMeasurementUnitScreenDestination.route) {
            NewMeasurementUnitScreen(
                openDrawer = { scope.launch { drawerState.open() } },
                navigateToMeasurementUnitListScreen = {
                    navController.navigate(MeasurementUnitListScreenDestination.route)
                }
            )
        }
        composable(route = MeasurementUnitListScreenDestination.route) {
            MeasurementUnitListScreen(
                openDrawer = { scope.launch { drawerState.open() } },
                navigateToNewMeasurementUnitScreen = {
                    navController.navigate(NewMeasurementUnitScreenDestination.route)
                }
            )
        }
    }
}
