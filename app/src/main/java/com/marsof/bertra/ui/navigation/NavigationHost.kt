package com.marsof.bertra.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.marsof.bertra.ui.screens.HomeScreen
import com.marsof.bertra.ui.screens.HomeScreenDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The list of the destination points for the Navigation Host.
 */
val navigationDestinations = listOf<INavigationDestination>(
    HomeScreenDestination,
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
                navigateToTaskForm = {
//                    navController.navigate(TaskFormDestination.route)
                },
                openDrawer = { scope.launch { drawerState.open() } }
            )
        }
//        composable(route = TaskFormDestination.route) {
//            TaskFormScreen(
//                navigateUp = {
//                    navController.navigateUp()
//                }
//            )
//        }
    }
}
