package com.marsof.bertra.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.marsof.bertra.ui.screens.HomeScreen
import com.marsof.bertra.ui.screens.HomeScreenDestination
import com.marsof.bertra.ui.screens.NewTrainScreen
import com.marsof.bertra.ui.screens.NewTrainScreenDestination
import com.marsof.bertra.ui.screens.TrainExercisesScreen
import com.marsof.bertra.ui.screens.TrainExercisesScreenDestination
import com.marsof.bertra.ui.screens.TrainListScreen
import com.marsof.bertra.ui.screens.TrainListScreenDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The list of the destination points for the Navigation Host.
 */
val navigationDestinations = listOf(
    HomeScreenDestination,
    TrainListScreenDestination,
    NewTrainScreenDestination,
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
        composable(route = TrainExercisesScreenDestination.route) {
            TrainExercisesScreen(
                openDrawer = { scope.launch { drawerState.open() } },
                navigateToScreen = {}
            )
        }
        composable(route = NewTrainScreenDestination.route) {
            NewTrainScreen(
                openDrawer = { scope.launch { drawerState.open() } },
                navigateToTrainExercisesScreen = {
                    navController.navigate(TrainExercisesScreenDestination.route)
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
    }
}
