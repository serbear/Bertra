package com.marsof.bertra.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.R
import com.marsof.bertra.SettingsDataStore
import com.marsof.bertra.ui.ViewModelProvider
import com.marsof.bertra.ui.elements.ApplicationTopBar
import com.marsof.bertra.ui.navigation.INavigationDestination
import com.marsof.bertra.ui.viewmodels.SettingsScreenViewModel
import kotlinx.coroutines.launch

object SettingsScreenDestination : INavigationDestination {
    private const val ROUTE_NAME = "settings"
    override val route: String get() = ROUTE_NAME
    override val titleRes: Int get() = R.string.settings_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = viewModel(
        factory = ViewModelProvider.AppViewModelProvider
    ),
    openDrawer: () -> Unit,
) {

    Scaffold(
        topBar = {
            ApplicationTopBar(
                title = stringResource(SettingsScreenDestination.titleRes),
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
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
            ) {
                val settingsDataStore = SettingsDataStore(context = LocalContext.current)
                SettingsSlider(settingsDataStore = settingsDataStore)
                ThemeSwitch(
                    settingsDataStore = settingsDataStore,
                    onSwitch = viewModel::changeTheme,
                )
            }
        }
    }
}

@Composable
fun SettingsSlider(settingsDataStore: SettingsDataStore) {
    val coroutineScope = rememberCoroutineScope()
    val defaultCirclesNumber by settingsDataStore.defaultCirclesNumber.collectAsState(initial = 1)
    var sliderPosition by remember(defaultCirclesNumber) {
        mutableFloatStateOf(defaultCirclesNumber.toFloat())
    }
    val sliderLabelText = stringResource(id = R.string.settings_default_circles_number_label)
    val sliderPositionText = sliderPosition.toInt()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "$sliderLabelText $sliderPositionText"
        )
        Slider(
            value = sliderPosition,
            onValueChange = { newValue ->
                // Update the slider position immediately for smoothness.
                sliderPosition = newValue
            },
            onValueChangeFinished = {
                // Save the slide value after user finishes dragging.
                coroutineScope.launch {
                    settingsDataStore.setDefaultCirclesNumber(sliderPosition.toInt())
                }
            },
            valueRange = 1f..99f,
            steps = 97 // (99 - 1) - 1 = 97 steps between values
        )
    }
}

@Composable
fun ThemeSwitch(
    settingsDataStore: SettingsDataStore,
    onSwitch: (Boolean) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val isDarkTheme by settingsDataStore.isDarkMode.collectAsState(initial = false)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.settings_dark_mode_switch)
        )
        Switch(
            checked = isDarkTheme,
            onCheckedChange = {
//                coroutineScope.launch {
                //settingsDataStore.setDarkMode(it)
                onSwitch(it)

//                }
            }
        )
    }
}