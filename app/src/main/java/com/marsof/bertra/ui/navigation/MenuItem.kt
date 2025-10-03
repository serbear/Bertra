package com.marsof.bertra.ui.navigation

/**
 * Represents the menu item in the side menu.
 * It can be a destination item or a separator.
 */
sealed interface MenuItem

/**
 * Represents the menu item which leads to a concrete screen.
 */
data class DestinationMenuItem(val destination: INavigationDestination): MenuItem

/**
 * The divider in the menu.
 */
data object DividerMenuItem : MenuItem