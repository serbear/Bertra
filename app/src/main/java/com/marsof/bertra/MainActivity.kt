package com.marsof.bertra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.marsof.bertra.ui.navigation.DestinationMenuItem
import com.marsof.bertra.ui.navigation.DividerMenuItem
import com.marsof.bertra.ui.navigation.NavigationHost
import com.marsof.bertra.ui.navigation.sideMenuDestinations
import com.marsof.bertra.ui.theme.BertraTheme
import com.marsof.bertra.ui.theme.LocalCustomColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val settingsManager = SettingsDataStore(context)
            val isDarkTheme by settingsManager.isDarkMode.collectAsState(initial = false)

            BertraTheme(
                darkTheme = isDarkTheme,
            ) {
                DrawerWithNavigation()
            }
        }
    }
}

@Composable
fun DrawerWithNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentDestination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RoundedCornerShape(0.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(LocalCustomColors.current.primary)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bear),
                        contentDescription = stringResource(R.string.api_description),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .graphicsLayer(
                                translationY = 0f,
                                transformOrigin = TransformOrigin(0.5f, 0f),
                            )
                    )
                    Text(
                        text = stringResource(R.string.main_menu_name),
                        style = MaterialTheme.typography.headlineSmall,
                        color = LocalCustomColors.current.textPrimary,
                        modifier = Modifier.padding(16.dp),
                    )
                    MenuItems(
                        scope = scope,
                        drawerState = drawerState,
                        navController = navController,
                        currentRoute = currentRoute
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.copyright),
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    )
                }
            }
        }
    ) {
        NavigationHost(
            navController = navController,
            scope = scope,
            drawerState = drawerState,
        )
    }
}

@Composable
fun MenuItems(
    scope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavHostController,
    currentRoute: String?
) {
    sideMenuDestinations.forEach { item ->
        when (item) {
            is DestinationMenuItem -> {
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = stringResource(item.destination.titleRes),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    selected = currentRoute == item.destination.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(item.destination.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    },
                    shape = RoundedCornerShape(0.dp),
                )
            }

            is DividerMenuItem -> {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 0.dp),
                    color = LocalCustomColors.current.secondary,
                )
            }
        }
    }
}
