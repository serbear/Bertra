package com.marsof.bertra.ui.elements

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * The general top bar using in the application screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationTopBar(
    title: String,
    onNavigationClick: () -> Unit,
    navigationIcon: ImageVector = Icons.Default.Menu,
    navigationContentDescription: String? = "Open the Main Menu",
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationContentDescription,
                )
            }
        },
        actions = actions,
        colors = colors,
    )
}